package com.fcu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QaforumApplication {

	public static void main(String[] args) {
		SpringApplication.run(QaforumApplication.class, args);
	}

}
