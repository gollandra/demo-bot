package ru.test.command.tgbot.demobot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "ru.test.command.tgbot.demobot.repository")
public class DemoBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoBotApplication.class, args);
	}
}
