package com.jubl.food.nextgendiscovery.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents the site-properties data.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NameValuePair implements Serializable {

    /**
     * Serializable identifier.
     */
    private static final long serialVersionUID = 8478393455780446501L;

    /**
     * the name of property.
     */
    private String name;

    /**
     * the value of property.
     */
    private String value;

}
