package com.intership.file.share;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {
		"com.intership.file.share.files.management",
		"com.intership.file.share.auth.user"
})


@EnableJpaRepositories(basePackages = {
		"com.intership.file.share.files.management",
		"com.intership.file.share.auth.Repository"
})

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
