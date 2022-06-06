package com.jubl.food.nextgendiscovery.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the platform data.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Platform implements Serializable {

    /**
     * Serializable identifier.
     */
    private static final long serialVersionUID = 8535595322514444408L;

    /**
     * The platform id.
     */
    private Integer id;

    /**
     * The name of platform.
     */
    private String name;

    /**
     * The link rewrite for platform.
     */
    private String linkRewrite;

    /**
     * The created at timestamp.
     */
    private Long createdAt;

    /**
     * The updated at timestamp.
     */
    private Long updatedAt;
}
