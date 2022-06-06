package com.jubl.food.nextgendiscovery.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Currency implements Serializable {

    private static final long serialVersionUID = 4736880248711497835L;

    /**
     * The id of the currency.
     */
    private Integer id;

    /**
     * the name of the currency.
     */
    private String name;

    /**
     * The isoCode of the currency.
     */
    @JsonAlias(value = {"iso_code"})
    private String isoCode;

    /**
     * The sign of the currency.
     */
    private String sign;
    private int blank;
    private long conversionRate;
    private String createdAt;
    private int decimals;
    private String deletedAt;
    private int format;
    private int isoCodeNum;
    private String updatedAt;
}
