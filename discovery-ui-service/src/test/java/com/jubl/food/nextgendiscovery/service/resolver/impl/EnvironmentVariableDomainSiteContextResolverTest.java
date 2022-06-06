package com.jubl.food.nextgendiscovery.service.resolver.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class EnvironmentVariableDomainSiteContextResolverTest {
    
    @InjectMocks
    private EnvironmentVariableDomainSiteContextResolver contextResolver;

    
    @Mock
    private ObjectMapper objectMapper;
    
    @BeforeEach
     void init() throws JsonProcessingException {
        MockitoAnnotations.openMocks(this);
        //objectMapper = mock(ObjectMapper.class);
        contextResolver=new EnvironmentVariableDomainSiteContextResolver("{\"localhost\":\"jflcms\"}", new ObjectMapper());

    }
    
    @Test
    public void testAfterPropertiesSet() throws Exception {
        assertDoesNotThrow(()-> contextResolver.afterPropertiesSet());
    }

    @Test
    public void testResolve() {
        HttpServletRequest  mockedRequest = mock(HttpServletRequest.class);
        StringBuffer s=new StringBuffer("http://localhost:8080/test");
        when(mockedRequest.getRequestURL()).thenReturn(s);
        assertEquals("jflcms", contextResolver.resolve(mockedRequest));
    }

    @Test
    public void testResolveHost() {
        assertEquals("localhost", contextResolver.resolveHost("jflcms"));
    }

}
