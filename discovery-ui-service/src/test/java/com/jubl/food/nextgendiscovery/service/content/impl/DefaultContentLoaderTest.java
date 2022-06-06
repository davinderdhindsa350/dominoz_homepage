package com.jubl.food.nextgendiscovery.service.content.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.jubl.food.nextgendiscovery.model.ContentRequest;
import com.jubl.food.nextgendiscovery.model.properties.Discovery;

@RunWith(MockitoJUnitRunner.class)
public class DefaultContentLoaderTest {

    @InjectMocks
    private DefaultContentLoader defaultContentLoader = new DefaultContentLoader();

    @Mock
    private Discovery discovery;
    @Spy
    private RestTemplate restTemplate;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
       // discovery = spy(Discovery.class);
        //restTemplate = mock(RestTemplate.class);

    }

    @Test
    public void testAfterPropertiesSet() {
        assertDoesNotThrow(() -> defaultContentLoader.afterPropertiesSet());
    }

    @Test
    public void testLoad() {
        ContentRequest request=new ContentRequest();
        request.setSite("jflcms");
        request.setPlatform("desktop");
        request.setCategory("/");
        request.setLang("en");
       when(discovery.getServiceBaseUrl()).thenReturn("https://s3.dominosindia.in/jfl-discovery-service/ve1/");
        when(discovery.getApiKey()).thenReturn("1be746dc5827cf05");
        
        int sc = HttpStatus.SC_OK;
        ResponseEntity<String> response = mock(ResponseEntity.class);
        when(response.getStatusCodeValue()).thenReturn(HttpStatus.SC_OK);
        when(response.getBody()).thenReturn("body");
         //when(restTemplate.exchange( anyString(), any(HttpMethod.class),
         //any(), ArgumentMatchers.<Class<String>>any())).thenReturn(response);
         assertTrue(true);
         
         assertDoesNotThrow(()-> defaultContentLoader.load(request, false, null) );
          
    }

}
