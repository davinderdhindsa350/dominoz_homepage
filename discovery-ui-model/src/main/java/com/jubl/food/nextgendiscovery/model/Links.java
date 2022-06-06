package com.jubl.food.nextgendiscovery.model;

import java.io.Serializable;

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
public class Links implements Serializable {

    /**
     * Serializable identifier.
     */
    private static final long serialVersionUID = -4964349520518010218L;

    private String rel;
    private String href;
}
