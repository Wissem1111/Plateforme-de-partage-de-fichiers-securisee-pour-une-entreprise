package com.intership.file.share;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
		"com.intership.file.share.users.management",
		"com.intership.file.share.roles.management"
})
@EnableJpaRepositories(basePackages = {
		"com.intership.file.share.users.management",
		"com.intership.file.share.roles.management"
})
public class Application {
    public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
