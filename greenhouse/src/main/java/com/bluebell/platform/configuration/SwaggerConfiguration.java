package com.bluebell.platform.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration for Swagger documentation
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {

        Server localDev = new Server();
        localDev.setUrl("http://localhost:8080");
        localDev.setDescription("Local Development");

        Contact myContact = new Contact();
        myContact.setName("Stephen Prizio");
        myContact.setEmail("s.prizio@bluebell.com");

        Info information = new Info()
                .title("bluebell - Greenhouse API")
                .version("0.1.6")
                .description(
                        """
                        bluebell Wealth Management is a software system designed to track trading accounts and their performance, as well as evaluate trading strategies in order to evolve and adapt strategies
                        to maintain their effectiveness. bluebell comprises both a front-end project (flower) and back-end project (greenhouse), each comprises numerous modules, each with different functionalities. bluebell is
                        built on a MariaDB ORM and is integrated with React.js and Java Spring Boot.
                        """
                )
                .contact(myContact);

        return new OpenAPI().info(information).servers(List.of(localDev));
    }
}
