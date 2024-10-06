package com.dependencyresolver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class DependencyResolverApplication {

	public static void main(String[] args) {
		SpringApplication.run(DependencyResolverApplication.class, args);
	}

}
