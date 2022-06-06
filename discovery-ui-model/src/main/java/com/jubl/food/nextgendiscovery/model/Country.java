package com.jubl.food.nextgendiscovery.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the country class.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Country implements Serializable {

    private static final long serialVersionUID = -7954535106933450632L;

    /**
     * The id of the country.
     */
    private Integer id;

    /**
     * the call prefix or the isd code for the country.
     */
    @JsonAlias(value = {"call_prefix"})
    private String callPrefix;

    /**
     * The isoCode of the country.
     */
    @JsonAlias(value = {"iso_code"})
    private String isoCode;

}
