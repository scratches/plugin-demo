package com.example;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "run", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class ExampleMojo extends AbstractMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		System.err.println("Hi");
	}

}
