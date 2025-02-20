package kh.dev.payment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class PaymentServiceApplication {

	public static void main(String[] args) {
		// Set default time zone to UTC
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		SpringApplication.run(PaymentServiceApplication.class, args);
	}

}
