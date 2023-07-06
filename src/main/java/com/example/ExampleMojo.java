package com.example;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.guice.module.SpringModule;

@Mojo(name = "run", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class ExampleMojo extends AbstractMojo {

	private Service service;

	@Inject
	public ExampleMojo(Service service) {
		this.service = service;
	}

	public void execute() throws MojoExecutionException, MojoFailureException {
		service.say();
	}

}

interface Service {
	void say();
}

class DefaultService implements Service {
	DefaultService() {
		System.err.println("DefaultService");
	}

	public void say() {
		System.err.println("Hi");
	}
}

@Configuration
class MyConfiguration {
	@Bean
	public Service service() {
		return new DefaultService();
	}
}

@Named
class MyModule extends SpringModule {
	public MyModule() {
		super(SpringApplication.run(MyConfiguration.class));
	}
}