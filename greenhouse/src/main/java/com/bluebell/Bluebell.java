package com.bluebell;

import com.bluebell.platform.util.OSUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The main runner for greenhouse
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class Bluebell {

    public static void main(final String[] args) {
        OSUtil.generateEnv(args);
        SpringApplication.run(Bluebell.class, args);
    }
}
