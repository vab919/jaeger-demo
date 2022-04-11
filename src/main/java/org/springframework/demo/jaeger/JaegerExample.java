package org.springframework.demo.jaeger;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;

public final class JaegerExample {

	private final Tracer tracer;

	public JaegerExample(OpenTelemetry openTelemetry) {
		tracer = openTelemetry.getTracer("io.opentelemetry.example.JaegerExample");
	}

	private void myWonderfulUseCase() {
		// Generate a span
		Span span = this.tracer.spanBuilder("Jaeger Sample Use Case").startSpan();
		span.addEvent("Event 0");
		// execute my use case - here we simulate a wait
		doWork();
		span.addEvent("Event 1");
		span.end();
	}

	private void doWork() {
		try {
			Thread.sleep(1000);
		}
		catch (InterruptedException e) {
			// do the right thing here
		}
	}

	public static void main(String[] args) {
		// Parsing the input
		if (args.length < 1) {
			System.out.println("Missing [endpoint]");
			System.exit(1);
		}
		String jaegerEndpoint = args[0];

		// initialize SDK as early as possible in application's lifecycle
		OpenTelemetry openTelemetry = JaegerTelemetryConfiguration.initOpenTelemetry(jaegerEndpoint);

		// Start the example
		JaegerExample example = new JaegerExample(openTelemetry);
		// generate a few sample spans
		for (int i = 0; i < 10; i++) {
			example.myWonderfulUseCase();
		}

		System.out.println("Bye");
	}

}
