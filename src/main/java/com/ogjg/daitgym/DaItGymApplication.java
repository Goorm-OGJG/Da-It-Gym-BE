package com.ogjg.daitgym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DaItGymApplication {

    public static void main(String[] args) {
        SpringApplication.run(DaItGymApplication.class, args);
    }

}
