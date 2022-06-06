package com.jubl.food.nextgendiscovery.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the meta data for the site.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Meta implements Serializable {

    /**
     * Serializable identifier.
     */
    private static final long serialVersionUID = -4964349520518010218L;

    /**
     * The site meta data.
     */
    private Site site;

    /**
     * The category meta data.
     */
    private Category category;

    /**
     * The language meta data.
     */
    private Lang lang;

    /**
     * The theme meta data.
     */
    private Theme theme;

    /**
     * The platform meta data.
     */
    private Platform platform;

    /**
     * The site properties.
     */
    private List<NameValuePair> props;
}
