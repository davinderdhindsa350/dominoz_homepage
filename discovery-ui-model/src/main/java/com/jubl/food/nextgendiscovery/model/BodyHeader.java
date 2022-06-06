package com.jubl.food.nextgendiscovery.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * Represents the body header.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
public class BodyHeader implements Serializable {

    /**
     * Serializable identifier.
     */
    private static final long serialVersionUID = 6923427161072617093L;
}
