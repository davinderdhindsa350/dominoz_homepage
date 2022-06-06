package com.jubl.food.nextgendiscovery.model;

import java.io.Serializable;

import com.jubl.food.nextgendiscovery.model.response.ParseSeoUrlResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Content load request.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentRequest implements Serializable {

    /**
     * Serializable identifier.
     */
    private static final long serialVersionUID = -1428651484846273796L;

    /**
     * The site.
     */
    private String site;

    /**
     * The platform.
     */
    private String platform;

    /**
     * The category.
     */
    private String category;

    /**
     * The language.
     */
    private String lang;

    /**
     * The category id.
     */
    private String storeId;

    /**
     * The platform id.
     */
    private Integer platformId;

    /**
     * The kiwi channel.
     */
    private Integer kiwiChannel;

    /**
     * The kiwi sub channel.
     */
    private Integer kiwiSubChannel;

    /**
     * The site domain.
     */
    private String domain;

    /**
     * The site domain.
     */
    private String country;

    /**
     * The E etag.
     */
    private String eTag;

    /**
     * Parse SEO response.
     */
    private ParseSeoUrlResponse parseSeoUrlResponse;

    /**
     * Create instance with specified site, platform, category, lang.
     *
     * @param siteIn     the site name
     * @param platformIn   the platform name
     * @param categoryIn the category name
     * @param langIn     the language name
     */
    public ContentRequest(final String siteIn, final String platformIn, final String categoryIn, final String langIn) {
        this.site = siteIn;
        this.platform = platformIn;
        this.category = categoryIn;
        this.lang = langIn;
    }
}
