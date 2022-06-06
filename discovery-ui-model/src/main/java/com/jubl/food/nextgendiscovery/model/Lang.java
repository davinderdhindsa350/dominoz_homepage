package com.jubl.food.nextgendiscovery.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the lang data.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lang implements Serializable {

    /**
     * Serializable identifier.
     */
    private static final long serialVersionUID = 552160878824199445L;

    /**
     * The language id.
     */
    private Integer id;

    /**
     * The language name.
     */
    private String name;

    /**
     * The lang iso code.
     */
    private String isoCode;

    /**
     * The lang iso code.
     */
    private String languageCode;

    /**
     * The date format lite.
     */
    private String dateFormatLite;

    /**
     * The date format full.
     */
    private String dateFormatFull;

    /**
     * The created at timestamp.
     */
    private String createdAt;

    /**
     * The updated at timestamp.
     */
    private String updatedAt;

    /**
     * Sets the value of id.
     * @param idIn
     * @return The value of id to be set
     */
    public Lang setId(final Integer idIn) {
        this.id = idIn;
        return this;
    }

    /**
     * Sets the value of name.
     * @param nameIn
     * @return The value of name to be set
     */
    public Lang setName(final String nameIn) {
        this.name = nameIn;
        return this;
    }

  /**
     * Sets the value of isoCode.
     * @param isoCodeIn
     * @return The value of isoCode to be set
     */
    public Lang setIsoCode(final String isoCodeIn) {
        this.isoCode = isoCodeIn;
        return this;
    }

    /**
     * Sets the value of languageCode.
     * @param languageCodeIn
     * @return The value of languageCode to be set
     */
    public Lang setLanguageCode(final String languageCodeIn) {
        this.languageCode = languageCodeIn;
        return this;
    }

    /**
     * Sets the value of dateFormatLite.
     * @param dateFormatLiteIn
     * @return The value of dateFormatLite to be set
     */
    public Lang setDateFormatLite(final String dateFormatLiteIn) {
        this.dateFormatLite = dateFormatLiteIn;
        return this;
    }

    /**
     * Sets the value of dateFormatFull.
     * @param dateFormatFullIn
     * @return The value of dateFormatFull to be set
     */
    public Lang setDateFormatFull(final String dateFormatFullIn) {
        this.dateFormatFull = dateFormatFullIn;
        return this;
    }

    /**
     * Sets the value of createdAt.
     * @param createdAtIn
     * @return The value of createdAt to be set
     */
    public Lang setCreatedAt(final String createdAtIn) {
        this.createdAt = createdAtIn;
        return this;
    }


    /**
     * Sets the value of updatedAt.
     * @param updatedAtIn
     * @return The value of updatedAt to be set
     */
    public Lang setUpdatedAt(final String updatedAtIn) {
        this.updatedAt = updatedAtIn;
        return this;
    }
}
