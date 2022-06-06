package com.jubl.food.nextgendiscovery.service.content.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.jubl.food.nextgendiscovery.model.ContentRequest;
import com.jubl.food.nextgendiscovery.model.properties.Discovery;

@RunWith(MockitoJUnitRunner.class)
public class ConstantDataLoaderTest {
    
    @InjectMocks
    ConstantDataLoader constantDataLoader=new ConstantDataLoader();

    @Mock
    private Discovery discovery;
    @Spy
    private RestTemplate restTemplate;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testConstantDataLoader() {
        when(discovery.getServiceBaseUrl()).thenReturn("https://s3.dominosindia.in/jfl-discovery-service/ve1/");
        when(discovery.getApiKey()).thenReturn("1be746dc5827cf05");
        
        int sc = HttpStatus.SC_OK;
         
         assertDoesNotThrow(()-> constantDataLoader.constantDataLoader() );
          
    }

}
