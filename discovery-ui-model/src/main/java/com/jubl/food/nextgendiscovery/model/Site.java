package com.jubl.food.nextgendiscovery.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto to represent site data.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Site implements Serializable {

    /**
     * Serializable identifier.
     */
    private static final long serialVersionUID = 5100461004011340854L;

    /**
     * The site id.
     */
    private Integer id;

    /**
     * The name of site.
     */
    private String name;

    /**
     * Whether the site is under maintenance or not.
     */
    private Integer underMaintenance;

    /**
     * The site domain.
     */
    private String domain;

    /**
     * The site context.
     */
    private String context;

    /**
     * The site favicon.
     */
    private String favicon;

    /**
     * The site lang count.
     */
    private Integer langCount;

    /**
     * The created at timestamp.
     */
    private String createdAt;

    /**
     * The updated at timestamp.
     */
    private String updatedAt;

    /**
     * The name of title.
     */
    private String title;
}
