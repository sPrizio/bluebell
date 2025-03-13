package com.bluebell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The main runner for greenhouse
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class Bluebell {

    public static void main(String[] args) {
        SpringApplication.run(Bluebell.class, args);
    }
}
