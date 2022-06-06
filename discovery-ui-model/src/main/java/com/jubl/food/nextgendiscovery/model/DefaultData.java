package com.jubl.food.nextgendiscovery.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a DefaultData.
 *
 * @author Davinder Singh
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultData {
    private int categoryId;
    private int langId;
    private int platformId;
    private int currencyId;
}
