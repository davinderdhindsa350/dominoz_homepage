package com.jubl.food.nextgendiscovery.service.util;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
//import org.joda.time.LocalDate;

import com.jubl.food.nextgendiscovery.model.exception.BadRequestException;

/**
 * Common validation utility class.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
public final class ValidationUtil {

    /**
     * We don't want anyone to instantiate this.
     */
    private ValidationUtil() {
    }

    /**
     * Assert that the arguments are not null.
     *
     * @param args the arguments to check for nullability
     */
    public static void assertNotNull(final Object... args) {
        final AtomicInteger index = new AtomicInteger();
        Arrays.stream(args).
                forEach(arg -> assertNotNull(arg, String.format("argument at index {%d} must not be null", index.incrementAndGet())));
    }

    /**
     * Assert that variable is not null.
     *
     * @param arg     the argument to validate
     * @param message the message if argument is invalid
     */
    public static void assertNotNull(final Object arg, final String message) throws IllegalArgumentException {
        if (arg == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert the boolean condition to be true.
     *
     * @param condition the condition to be true
     * @param message   the error message
     * @throws BadRequestException if the condition is not true
     */
    public static void assertTrue(final Boolean condition, final String message) throws IllegalArgumentException {
        if (Boolean.FALSE.equals(condition)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert that variable is valid.
     *
     * @param arg     the argument to validate
     * @param message the message if argument is invalid
     */
    public static void assertValid(final String arg, final String message) throws IllegalArgumentException {
        if (StringUtils.isBlank(arg)) {
            throw new IllegalArgumentException(message);
        }
    }

}
