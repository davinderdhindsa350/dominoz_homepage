package com.jubl.food.nextgendiscovery.service.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class RequestUtilTest {
    MockHttpServletRequest request = new MockHttpServletRequest();
    @Test
    public void testGetCookieDomain() throws Exception {
        assertEquals("localhost", RequestUtil.getCookieDomain(request));
    }

    @Test
    public void testGetMediaPath() throws Exception {
        assertEquals("media.localhost", RequestUtil.getMediaPath(request));
    }

    @Test
    public void testGetDiscoveryURI() throws Exception {
        assertEquals("http://localhost/service", RequestUtil.getDiscoveryURI(request));
    }

}
