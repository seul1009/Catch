package com.catcher.catchApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatchApplication.class, args);
	}

}
