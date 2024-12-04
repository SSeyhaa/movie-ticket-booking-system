package com.legend.feedback_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class FeedbackServiceApplication {

	public static void main(String[] args) {
		// Set default time zone to UTC
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		SpringApplication.run(FeedbackServiceApplication.class, args);
	}

}
