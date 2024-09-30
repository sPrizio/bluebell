package com.bluebell.planter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The base spring main server execution method
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
@SpringBootApplication
public class Planter {

    public static void main(String[] args) {
        SpringApplication.run(Planter.class, args);
    }

}
