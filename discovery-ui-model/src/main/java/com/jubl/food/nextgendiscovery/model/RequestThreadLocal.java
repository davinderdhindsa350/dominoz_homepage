package com.jubl.food.nextgendiscovery.model;

import lombok.Builder;
import lombok.Data;

/**
 * Thread local containing request attribues.
 */

@Data
@Builder
public final class RequestThreadLocal {

    /**
     * Thread local containing request detail container.
     */
    private static final ThreadLocal<RequestDetailContainer> REQUEST_CONTAINER = new ThreadLocal<>();

    /**
     * Set Request detail container in tread local.
     *
     * @param requestDetailContainer Request detail container
     */
    public static void setRequsetContainer(final RequestDetailContainer requestDetailContainer) {
        REQUEST_CONTAINER.set(requestDetailContainer);
    }

    /**
     * Fetch requested url from thread local.
     *
     * @return requested url from thread local
     */
    public static String getRequestUrl() {
        if (REQUEST_CONTAINER.get() == null) {
            return null;
        }
        return REQUEST_CONTAINER.get().getRequestUrl();
    }

    /**
     * Fetch domain from thread local.
     *
     * @return domain from thread local
     */
    public static String getDamain() {
        if (REQUEST_CONTAINER.get() == null) {
            return null;
        }
        return REQUEST_CONTAINER.get().getDomain();
    }

    /**
     * Fetch request context from thread local.
     *
     * @return request context from thread local
     */
    public static String getRequestContext() {
        if (REQUEST_CONTAINER.get() == null) {
            return null;
        }
        return REQUEST_CONTAINER.get().getContextPath();
    }

    /**
     * get Site Context.
     * @return getSiteContext
     */
    public static String getSiteContext() {
        if (REQUEST_CONTAINER.get() == null) {
            return null;
        }
        return REQUEST_CONTAINER.get().getSiteContext();
    }

    /**
     * get Source Country.
     * @return getSourceCountry
     */
    public static String getSourceCountry() {
        if (REQUEST_CONTAINER.get() == null) {
            return null;
        }
        return REQUEST_CONTAINER.get().getSourceCountry();
    }

    /**
     * get Request DetailContainer.
     * @return RequestDetailContainer
     */
    public static RequestDetailContainer getRequestDetailContainer() {
        return REQUEST_CONTAINER.get();
    }

    /**
     * unload.
     */
    public void unload() {
        REQUEST_CONTAINER.remove();
    }

    private RequestThreadLocal() {
        throw new IllegalStateException("Constants class");
    }

}
