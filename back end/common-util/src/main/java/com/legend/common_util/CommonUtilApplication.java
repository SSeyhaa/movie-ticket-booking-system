package com.legend.common_util;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CommonUtilApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommonUtilApplication.class, args);
	}

}
