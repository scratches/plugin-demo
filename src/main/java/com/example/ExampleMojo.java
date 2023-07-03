package com.example;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

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

class MyModule extends AbstractModule {
	public MyModule() {
		System.err.println("MyModule");
	}

	@Override
	protected void configure() {
		bind(Service.class).to(DefaultService.class);
	}
}

@Named
class InjectorProvider implements Provider<Injector> {
	private final Injector injector;

	@Inject
	InjectorProvider(MavenSession session) {
		injector = Guice.createInjector(new MyModule());
	}

	public Injector get() {
		return injector;
	}
}

@Named
class ServiceProvider implements Provider<Service> {
	private final Injector injector;

	@Inject
	ServiceProvider(Injector injector) {
		this.injector = injector;
	}

	public Service get() {
		return injector.getInstance(Service.class);
	}
}