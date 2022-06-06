package com.jubl.food.nextgendiscovery.service.resolver;

import com.jubl.food.nextgendiscovery.model.Country;
import com.jubl.food.nextgendiscovery.model.Currency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * The resolve response.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
public class ResolveResponse {

    /**
     * The platform.
     */
    private final String platform;

    /**
     * The lang.
     */
    private final String lang;

    /**
     * Represents name of the Site.
     */
    private final String category;

    /**
     * Represents the Country attributes.
     */
    private final Country country;

    /**
     * Represents the currency attributes for the site.
     */
    private final Currency currency;

    /**
     * Represents the value of country name.
     */
    private final String countryName;
}
