package com.example;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import com.google.inject.AbstractModule;

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

@Named
class DefaultService implements Service {
	public void say() {
		System.err.println("Hi");
	}
}

@Named
class MyModule extends AbstractModule {
	public MyModule() {
		System.err.println("MyModule");
	}
	@Override
	protected void configure() {
		bind(Service.class).to(DefaultService.class);
	}
}