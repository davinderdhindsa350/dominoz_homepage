package com.jubl.food.nextgendiscovery.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

import com.jubl.food.nextgendiscovery.model.properties.HttpClientVariables;


/**
 * Spring boot application for NextGen discovery.
 *
 * @author Davinder Singh
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.jubl.food.nextgendiscovery.model", "com.jubl.food.nextgendiscovery.service", "com.jubl.food.nextgendiscovery.web"})
@Configuration
@Import({ HttpClientVariables.class})
public class NextgendiscoveryApplication extends SpringBootServletInitializer  {

    /**
     * main.
     * @param args
     */
    public static void main(final String[] args) {
        SpringApplication.run(NextgendiscoveryApplication.class, args);
        }

    /**
     * Configure the spring servlet application using the spring application builder.
     * @param builder the builder
     * @return the configured {@link SpringApplicationBuilder}
     */
    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder builder) {
        return builder.sources(NextgendiscoveryApplication.class);
    }

    /**
     * restTesmplate.
     * @return RestTemplate
     */
    @Bean
    public RestTemplate restTesmplate() {
        return new RestTemplate();
    }
}
