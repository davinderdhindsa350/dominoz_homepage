package com.jubl.food.nextgendiscovery.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Content load response.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentResponse implements Serializable {

    /**
     * Serializable identifier.
     */
    private static final long serialVersionUID = 3674483401937505430L;

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
    private Integer categoryId;

    /**
     * The platform id.
     */
    private Integer platformId;

    /**
     * The site domain.
     */
    private String domain;

    /**
     * the Country object representing the country.
     */
    private Country country;

    /**
     * the Currency of the country.
     */
    private Currency currency;

    /**
     * Site properties to set.
     */
    private String properties;

    /**
     * Festive prop to set.
     */
    private String festivePropImage;

    /**
     * Festive prop to set.
     */
    private String festivePropBodyCss;

    /**
     * Festive prop to set.
     */
    private String festivePropHeaderCss;

    /**
     * Festive prop to set.
     */
    private String festivePropFooterCss;

    /**
     * the context.
     */
    private String context;

    /**
     * the favicon.
     */
    private String favicon;

    /**
     * whether site is under maintenance.
     */
    private Integer underMaintainance;

    /**
     * the profile.
     */
    private String profile;

}
