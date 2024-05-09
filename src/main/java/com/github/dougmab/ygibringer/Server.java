package com.github.dougmab.ygibringer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Server {

	private static ConfigurableApplicationContext context;
	private static boolean isRunning = false;

	public static void start(String[] args) {
		if (isRunning) return;

		context = SpringApplication.run(Server.class, args);
		isRunning = true;
	}

	public static void shutdown() {
		if (!isRunning) return;

		System.out.println("Shutdown Server...");
		context.close();
		isRunning = false;
	}

	public static boolean isRunning() {
		return isRunning;
	}
}
