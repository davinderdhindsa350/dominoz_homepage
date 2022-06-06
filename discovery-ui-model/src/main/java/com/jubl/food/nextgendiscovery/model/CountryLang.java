package com.jubl.food.nextgendiscovery.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class to represent default language.
 *
 * @author Davinder Singh
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryLang {

    /**
     * The country name.
     */
    private String name;

}
