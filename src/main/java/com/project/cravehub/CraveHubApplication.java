package com.project.cravehub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "com.project.craveHub")
public class CraveHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(CraveHubApplication.class, args);
	}


}
