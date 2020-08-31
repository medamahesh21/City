package com.city.route.example.city;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"com.city"})
@ComponentScan(basePackages = "com.city.*")
public class CityApplication {

	public static void main(String[] args) {

		SpringApplication.run(CityApplication.class, args);

	}

}
