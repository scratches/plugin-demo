This project has a sample Maven Plugin. It shows how to use [Eclipse Sisu](https://www.eclipse.org/sisu/) to inject dependencies into a Maven Plugin. The [official documentation](https://maven.apache.org/maven-jsr330.html) for Maven Plugins is a good place to start, and there is a Wiki page in [Sisu Plexus](https://github.com/eclipse/sisu.plexus/wiki/Plexus-to-JSR330) that has more detail.

## What Can You Do?

Using Sisu you can inject components from a `@Named` Guice `Provider` into a Maven plugin. The components can be injected into the plugin itself, or into another `@Named` class that is instantiated by the plugin. For example:

```java
@Named
class ServiceProvider implements Provider<Service> {
	public Service get() {
		return new DefaultService();
	}
}
```

Using the plugin in this project as [Maven Core extension](https://maven.apache.org/guides/mini/guide-using-extensions.html) you can also write a custom Guice `Module` and inject components from that module into your plugin. The plugin can be installed in a Maven repository and used in other projects. Annotate the `Module` with `Named` and it will be picked up automatically. For example:

```java
@Named
class MyModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Service.class).to(DefaultService.class);
	}
}
```

Guice has to be explicitly *not* on the runtime classpath for this to work - it comes from the parent classloader provided my Maven. Hence in the plugin we define that dependency as `provided`.

You can also (optionally) use Spring Guice to construct your plugin by using `SpringModule` instead of `AbstractModule`:

```java
@Named
class MyModule extends SpringModule {
	public MyModule() {
		super(SpringApplication.run(MyConfiguration.class));
	}
}
```

Spring and Guice have to be explicitly on the compile time classpath for this to work, but you have to exclude Guice from the plugin dependencies still, and you have to use Maven 4 for Spring Boot 3 (or Spring 6) because of Java 17.

## How Do You Do It?

You need to build and install the plugin first so it can be used as an extension, so `./mvnw install` should work. The build copies a Core extensions configuration file (`extensions.xml`) into `.mvn` telling Maven to use the plugin as an extension. The plugin itself has a `META-INF/maven/extension.xml`:

```xml
<extension>
	<exportedPackages>
		<exportedPackage>com.google.inject.*</exportedPackage>
		<exportedPackage>com.google.inject.binder.*</exportedPackage>
		<exportedPackage>com.google.inject.matcher.*</exportedPackage>
		<exportedPackage>com.google.inject.name.*</exportedPackage>
		<exportedPackage>com.google.inject.spi.*</exportedPackage>
		<exportedPackage>com.google.inject.util.*</exportedPackage>
	</exportedPackages>
</extension>
```

There is a sample at `src/it/sample` that you can use to verify that the plugin is running (it prints out "Hi" to stdout).

## How We Got Here

Unfortunately the Sisu Wiki page is [misleading or wrong](https://github.com/eclipse/sisu.plexus/issues/35) about a few things, so this project is a working example of how to do it. The parent project [Sisu Inject](https://github.com/eclipse/sisu.inject) _does_ already support custom bindings via a `@Named` Guice `Module`, but it doesn't work in a normal Maven plugin environment because the classloader boundaries are different (`Module` is in the parent classloader and is not exposed in the plugin).