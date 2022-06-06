package com.jubl.food.nextgendiscovery.model.exception;

import java.util.Map;

import com.jubl.food.nextgendiscovery.model.constants.ErrorCode;

/**
 * The root exception class for all the api's.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
public class ApiException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * The specified error code for this exception.
     * Error code is mandatory
     */
    private final String code;

    /**
     * Any additional information specific to this exception.
     */
    private final Map<String, Object> info;

    /**
     * Constructs a new instance of this exception with the specified detail message and
     * cause.
     *
     * @param message the detail message
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @since 1.0.0
     */
    public ApiException(final String message, final Throwable cause) {
        this(ErrorCode.UNKNOWN, message, cause);
    }

    /**
     * Constructs a new instance of this exception with the specified code and detail message.
     *
     * @param codeIn    the specified error code
     * @param messageIn the detail message
     * @since 1.0.0
     */
    public ApiException(final String codeIn, final String messageIn) {
        this(codeIn, messageIn, null);
    }

    /**
     * Constructs a new instance of this exception with the specified code, detail message and
     * cause.
     *
     * @param codeIn    the specified error code
     * @param messageIn the detail message
     * @param causeIn   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @since 1.0.0
     */
    public ApiException(final String codeIn, final String messageIn, final Throwable causeIn) {
        this(codeIn, messageIn, causeIn, null);
    }

    /**
     * Constructs a new instance of this exception with the specified code, detail message.
     * cause and additional info
     *
     * @param codeIn    the specified error code
     * @param messageIn the detail message
     * @param causeIn   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @param infoIn    the specified additional info
     * @since 1.0.0
     */
    public ApiException(final String codeIn, final String messageIn, final Throwable causeIn, final Map<String, Object> infoIn) {
        super(messageIn, causeIn);
        this.code = codeIn;
        this.info = infoIn;
    }

    /**
     * Gets code.
     *
     * @return value of code
     */
    public String getCode() {
        return code;
    }

    /**
     * Get the value of info.
     *
     * @return the value of info
     */
    public Map<String, Object> getInfo() {
        return info;
    }
}
