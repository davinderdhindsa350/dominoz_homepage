package com.jubl.food.nextgendiscovery.service.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ApiHeaderTest {
    
    private ApiHeader apiHeader;
    private  Map<String, String> headers;
    public static final String DEFAULT_LANGUAGE_CODE = "en";
    public static final String X_LANGUAGE_CODE = "x-language-code";

    @BeforeEach
    void init() {
        
        headers=new HashMap<String, String>();
        headers.put(X_LANGUAGE_CODE, DEFAULT_LANGUAGE_CODE);
        apiHeader=new ApiHeader(headers);
    }

    @Test
    public void testGetHeaders() {
        
        assertEquals(1, apiHeader.getHeaders().size());
    }

    @Test
    public void testGetHeader() {
        assertEquals(DEFAULT_LANGUAGE_CODE, apiHeader.getHeader(X_LANGUAGE_CODE));
    }

    @Test
    public void testDefaultHeaders() {
        assertEquals(DEFAULT_LANGUAGE_CODE, ApiHeader.defaultHeaders().getHeaders().get(X_LANGUAGE_CODE));
    }

}
