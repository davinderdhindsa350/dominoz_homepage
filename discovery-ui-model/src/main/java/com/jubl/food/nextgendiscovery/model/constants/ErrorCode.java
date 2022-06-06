package com.jubl.food.nextgendiscovery.model.constants;


/**
 * Application error codes.
 * Based on these error codes view is rendered
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
public final class ErrorCode {

    /**
     * External service invalid response.
     */
    public static final String EXTERNAL_SERVICE_INVALID_RESPONSE = "EXTERNAL_SERVICE_INVALID_RESPONSE";

    /**
     * Represents the error when server is failed to process the request.
     * This indicates that request is made successfully by the client and server has issues to process the request.
     * Should translate to 500 http status code
     */
    public static final String SERVER_ERROR = "SERVER_ERROR";

    /**
     * The error code to represent bad request from the client must translate to 400 http status code.
     */
    public static final String BAD_REQUEST = "BAD_REQUEST";

    /**
     * The error code is not known.
     */
    public static final String UNKNOWN = "UNKNOWN";

    /**
     * Indicates that external service returned non ok status code.
     * This error code means that client successfully called the external service but external service returned
     * non 2xx stats code
     */
    public static final String NON_OK_STATUS_CODE = "NON_OK_STATUS_CODE";

    /**
     * External service error.
     * Error the occur while calling the external service or while parsing the external service urls
     */
    public static final String EXTERNAL_SERVICE_ERROR = "EXTERNAL_SERVICE_ERROR";

    /**
     * Error code to indicate site context is not valid.
     */
    public static final String INVALID_SITE_CONTEXT = "INVALID_SITE_CONTEXT";

    private ErrorCode() {
        throw new IllegalStateException("ErrorCode class");
      }
}

