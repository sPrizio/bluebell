package com.bluebell.radicle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Executes the radicle module
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
@SpringBootApplication
@EnableJpaRepositories("com.bluebell.*")
@ComponentScan(basePackages = { "com.bluebell.*" })
@EntityScan("com.bluebell.*")
public class Radicle {

    public static void main(String[] args) {
        SpringApplication.run(Radicle.class, args);
    }
}