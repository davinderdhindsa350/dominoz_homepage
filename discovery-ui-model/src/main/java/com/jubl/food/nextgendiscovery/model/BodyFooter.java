package com.jubl.food.nextgendiscovery.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The body footer.
 *
 * @author Davinder Singh
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BodyFooter implements Serializable {

    /**
     * Serializable identifier.
     */
    private static final long serialVersionUID = -6643501117251721319L;

    /**
     * The footer content.
     */
    private List<HtmlContent> footerContent;

}
