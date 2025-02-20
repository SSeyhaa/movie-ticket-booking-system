package kh.dev.movie_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class MovieServiceApplication {

	public static void main(String[] args) {
		// Set default time zone to UTC
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		SpringApplication.run(MovieServiceApplication.class, args);
	}

}
