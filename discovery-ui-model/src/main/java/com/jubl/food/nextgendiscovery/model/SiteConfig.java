package com.jubl.food.nextgendiscovery.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a SiteConfig.
 *
 * @author Davinder Singh
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteConfig {

    private Site site;
    private DefaultData defaultData;
    private List<Platform> platformData;
    private List<Lang> lang;
    private List<Categories> categories;
    private Currency currency;
    private List<Country> country;

}
