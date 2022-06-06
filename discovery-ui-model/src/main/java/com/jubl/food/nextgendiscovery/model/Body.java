package com.jubl.food.nextgendiscovery.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The html body part.
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Body implements Serializable {

    /**
     * Serializable identifier.
     */
    private static final long serialVersionUID = 7210435127732559193L;

    /**
     * The body header.
     */
    private BodyHeader header;

    /**
     * The body content.
     */
    private BodyContent content;

    /**
     * The body footer.
     */
    private BodyFooter footer;
}
