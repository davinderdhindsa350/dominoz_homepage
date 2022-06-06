package com.jubl.food.nextgendiscovery.service.util;

import org.junit.jupiter.api.Test;

public class ValidationUtilTest {

    @Test
    public void testAssertNotNullObjectArray() {
        ValidationUtil.assertNotNull(new Object());
    }

    @Test
    public void testAssertNotNullObjectString() {
        ValidationUtil.assertNotNull(new Object(),"fail");
    }

    @Test
    public void testAssertTrue() {
        ValidationUtil.assertTrue(true, "fail");
    }

    @Test
    public void testAssertValid() {
        ValidationUtil.assertValid("test", "fail");
    }

}
