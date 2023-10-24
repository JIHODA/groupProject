package com.app.jihoproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JihoprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(JihoprojectApplication.class, args);
	}

}
