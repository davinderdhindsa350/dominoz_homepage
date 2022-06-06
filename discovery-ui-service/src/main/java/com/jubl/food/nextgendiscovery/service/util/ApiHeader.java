package com.jubl.food.nextgendiscovery.service.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Api common headers.
 * Inject this into any controller function to have access to common api headers
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
public class ApiHeader {

    /**
     * Default language code.
     */
    public static final String DEFAULT_LANGUAGE_CODE = "en";

    /**
     * language code header.
     * This header identifies the language of the request and response
     */
    public static final String X_LANGUAGE_CODE = "x-language-code";

    /**
     * source platform header.
     * This header identifies source platform
     * platform can be like, ios, android, web, web_mobile etc
     */
    public static final String X_SOURCE_PLATFORM = "x-source-platform";

    /**
     * The source platform version.
     */
    public static final String X_SOURCE_PLATFORM_VERSION = "x-source-platform-version";

    /**
     * The source country.
     * For some of the api's we do need an country identifier
     */
    public static final String X_SOURCE_COUNTRY = "x-source-country";

    /**
     * The source site.
     * This is the cms site context
     * For some of the api's we do need an site context identifier identifier
     */
    public static final String X_SOURCE_SITE_CONTEXT = "x-source-site-context";

    /**
     * The unique session id.
     */
    public static final String X_SESSION_ID = "x-session-id";

    /**
     * The device id in case of android or ios devices.
     */
    public static final String X_DEVICE_ID = "x-device-id";

    /**
     * The social provider.
     */
    public static final String X_SOCIAL_PROVIDER = "x-social-provider";

    /**
     * The social access token.
     */
    public static final String X_SOCIAL_ACCESS_TOKEN = "x-social-access-token";

    /**
     * source is profile or pop up.
     */
    public static final String X_SOURCE = "x-source";

    /**
     * rule applied.
     */
    public static final String X_RULE_APPLIED = "x-rule-applied";

    /**
     * rule version.
     */
    public static final String X_RULE_VERSION = "x-rule-version";

    /**
     * client id.
     */
    public static final String X_CLIENT_ID = "x-client-id";

    /**
     * The headers.
     */
    private final Map<String, String> headers;

    /**
     * Create an instance of this class with specified headers.
     *
     * @param headerMap the headers
     */
    public ApiHeader(final Map<String, String> headerMap) {
        if (headerMap == null) {
            throw new IllegalArgumentException("headers must not be null");
        }
        this.headers = headerMap;
    }

    /**
     * Get the value of headers.
     *
     * @return the value of headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Return the header value specified by the header argument name.
     *
     * @param headerName the header name to get value for
     * @return the header value is value is present otherwise null
     */
    public String getHeader(final String headerName) {
        ValidationUtil.assertValid(headerName, "header name must not be null/blank");
        return this.headers.get(headerName);
    }

    /**
     * Return the api headers which doesn't represent anything.
     *
     * @return the api header object with no headers
     */
    public static ApiHeader defaultHeaders() {
        Map<String, String> defaultLang = new HashMap<>();
        defaultLang.put(X_LANGUAGE_CODE, DEFAULT_LANGUAGE_CODE);
        return new ApiHeader(defaultLang);
    }
}
