package com.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class ExampleMojoTests {

	@Test
	public void test() throws Exception {
		AtomicInteger calls = new AtomicInteger(0);
		ExampleMojo mojo = new ExampleMojo(() -> {
			calls.incrementAndGet();
		});
		mojo.execute();
		assertThat(calls.get()).isEqualTo(1);
	}

}