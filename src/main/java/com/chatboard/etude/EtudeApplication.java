package com.chatboard.etude;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EtudeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EtudeApplication.class, args);
    }

}
