package com.tcode.moviebase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MovieBaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieBaseApplication.class, args);
    }

}
