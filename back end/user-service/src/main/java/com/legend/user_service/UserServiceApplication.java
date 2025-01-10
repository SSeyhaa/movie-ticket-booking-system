package com.legend.user_service;

import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class UserServiceApplication {

	public static void main(String[] args) {
		// Set default time zone to UTC
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		SpringApplication.run(UserServiceApplication.class, args);
	}

}
