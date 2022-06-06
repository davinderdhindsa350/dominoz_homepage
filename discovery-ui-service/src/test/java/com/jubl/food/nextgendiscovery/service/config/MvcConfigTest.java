package com.jubl.food.nextgendiscovery.service.config;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@RunWith(MockitoJUnitRunner.class)
public class MvcConfigTest {
    
    @InjectMocks
    private MvcConfig configuration=new MvcConfig();

    @Test
    public void testMessageSource() {
     assertNotNull(configuration.messageSource());
    }

    @Test
    public void testLocaleChangeInterceptor() {
     assertEquals("lang", configuration.localeChangeInterceptor().getParamName());
    }

    @Test
    public void testAddInterceptors() {
        InterceptorRegistry registry=new InterceptorRegistry();
        assertDoesNotThrow(()-> configuration.addInterceptors(registry));
    }

}
