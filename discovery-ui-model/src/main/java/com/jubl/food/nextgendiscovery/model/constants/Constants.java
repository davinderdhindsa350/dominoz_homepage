package com.jubl.food.nextgendiscovery.model.constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Constants {

    /**
     * The home view name.
     */
    public static final String HOME_VIEW_NAME = "views/home";

    /**
     * The jsp view name prefix.
     */
    public static final String JSP_VIEW_PREFIX = "/WEB-INF/";

    /**
     * The jsp view name suffix.
     */
    public static final String JSP_VIEW_SUFFIX = ".jsp";

    /**
     * Default theme of the site.
     */
    public static final String DEFAULT_THEME = "default";

    /**
     * Hard coded cookie domain.
     */
    public static final String DEFAULT_COOKIE_DOMAIN = "monsterindia.com";

    /**
     * SSO Client Cookie.
     */
    public static final String ACCESS_TOKEN_CLIENT_COOKIE_NAME = "MSSOCLIENT";

    /**
     * User poll cookie name.
     */
    public static final String USER_POLL_COOKIE_NAME = "MSPSI";

    /**
     * the poll taken header.
     */
    public static final String USER_POLL_HEADER_NAME = "x-poll-taken";

    /**
     * authorization code expires in seconds.
     * Default value is 30 days
     */
    public static final Integer AUTHORIZATION_CODE_EXPIRES_IN_SECONDS = 30 * 24 * 60 * 60;

    /**
     * SSO application name.
     * Media sub domain
     */
    public static final String MEDIA_SUB_DOMAIN = "media";

    /**
     * Rio application context path.
     */
    public static final String SSO_APPLICATION_NAME = "dominosindia";

    public static final String SEEKER_LOGIN_URL = "/login/seeker";

    public static final String LOGOUT_URL = "/logout";

    public static final String OTHER_LOCATION = "other";

    public static final String ACCESS_TOKEN_COOKIE_NAME = "MSSOAT";

    public static final String MSUID_COOKIE_NAME = "MSUID";

    public static final String REGEX_ESCAPE_CHAR = "\\";

    public static final String HYPHON_STRING = "-";

    public static final String MEDIA_SUFFIX = "/seo-search";

    /**
     * 500 error page view name.
     */
    public static final String BAD_REQUEST_VIEW_NAME = "views/error/400";

    /**
     * 500 error page view name.
     */
    public static final String SERVER_ERROR_VIEW_NAME = "views/error/500";

    /**
     * 404 error page view name.
     */
    public static final String NOT_FOUND_ERROR_VIEW_NAME = "views/error/404";

    /**
     * default error page view name.
     */
    public static final String DEFAULT_ERROR_VIEW_NAME = "views/error/500";

    /**
     * Bearer type access tokens.
     */
    public static final String BEARER_TYPE = "Bearer";

    /**
     * Basic type authorization header.
     */
    public static final String BASIC_TYPE = "Basic";

    /**
     * The site context.
     */
    public static final String SITE_CONTEXT_DEFAULT = "jflcms";

    /**
     * Load data path.
     */
    public static final String LOAD_DATA_PATH = "/api/public/sites/v1/load-data";
    public static final String CONSTANT_DATA_PATH = "/api/public/sites/v1/site-configs";
    public static final List<String> PLATFORMS = new ArrayList<>();
    public static final String CAUSE_ERROR_CLASS = "causeErrorClass";
    public static final String CAUSE_ERROR_MESSAGE = "causeErrorMessage";
    public static final String CAUSE_STACK_TRACE = "causeStackTrace";
    public static final String ERROR_DESCRIPTION = "error_description";
    public static final String ERROR_URL = "/error";

    /**
     * List of site context.
     */
    protected static Set<String> siteContexts = new HashSet<>();
    static {
        siteContexts.add(SITE_CONTEXT_DEFAULT);
        PLATFORMS.add("pwa");
        PLATFORMS.add("desktop");
        PLATFORMS.add("ios");
        PLATFORMS.add("android");
    }
    private Constants() {
        throw new IllegalStateException("Constants class");
      }


}
