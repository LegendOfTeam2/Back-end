package com.example.rhythme_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RhythmeBackEndApplication {
    String test= "";
    public static void main(String[] args) {
        SpringApplication.run(RhythmeBackEndApplication.class, args);
    }

}
