package com.example;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

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
@Singleton
class DefaultService implements Service {
	public void say() {
		System.err.println("Hi");
	}
}