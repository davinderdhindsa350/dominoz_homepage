package com.jubl.food.nextgendiscovery.service.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jubl.food.nextgendiscovery.model.properties.Discovery;

public class DiscoveryConfigurationTest {
    
    @InjectMocks
    private DiscoveryConfiguration configuration=new DiscoveryConfiguration();

    @Test
    public void testDomainSiteContextResolver() throws JsonMappingException, JsonProcessingException {
        Discovery discovery =new Discovery();
        discovery.setDomainSiteContextConfig("{\"localhost\":\"jflcms\"}");
        ObjectMapper objectMapper=new ObjectMapper();
        assertNotNull(configuration.domainSiteContextResolver(discovery, objectMapper));
    }

}
