package com.jubl.food.nextgendiscovery.service.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jubl.food.nextgendiscovery.model.properties.Discovery;
import com.jubl.food.nextgendiscovery.service.resolver.DomainSiteContextResolver;
import com.jubl.food.nextgendiscovery.service.resolver.impl.EnvironmentVariableDomainSiteContextResolver;

/**
 * Root configuration class.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Configuration
@ComponentScan(basePackages = "com.jubl.food")
@EnableAutoConfiguration
public class DiscoveryConfiguration implements WebMvcConfigurer {

    /**
     * @param discovery
     * @param objectMapper
     * @return DomainSiteContextResolver
     * @throws JsonProcessingException
     */
    @Bean
    public DomainSiteContextResolver domainSiteContextResolver(final Discovery discovery, final ObjectMapper objectMapper) throws JsonProcessingException   {
        return new EnvironmentVariableDomainSiteContextResolver(discovery.getDomainSiteContextConfig(), objectMapper);
    }
}
