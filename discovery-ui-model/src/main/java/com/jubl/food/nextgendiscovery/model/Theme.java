package com.jubl.food.nextgendiscovery.model;

import java.io.Serializable;
import java.util.List;

import com.jubl.food.nextgendiscovery.model.domain.meta.theme.Hook;
import com.jubl.food.nextgendiscovery.model.domain.meta.theme.Module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a theme.
 *
 * @author Davinder Singh
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Theme implements Serializable {

    /**
     * Serializable identifier.
     */
    private static final long serialVersionUID = 865248505125203111L;

    /**
     * The theme id.
     */
    private Integer id;

    /**
     * The theme name.
     */
    private String name;

    /**
     * The alias.
     */
    private String alias;

    /**
     * The theme directory.
     */
    private String directory;

    /**
     * The body class.
     */
    private String bodyClass;

    /**
     * The image preview.
     */
    private String imgPreview;

    /**
     * Created at timestamp.
     */
    private Long createdAt;

    /**
     * Updated at timestamp.
     */
    private Long updatedAt;

    /**
     * The header content of theme.
     */
    private String headerContent;

    /**
     * The footer content of theme.
     */
    private String footerContent;

    /**
     * The theme skeleton.
     */
    private String skeleton;

    /**
     * The hooks inside theme.
     */
    private List<Hook> hooks;

    /**
     * The modules inside theme.
     */
    private List<Module> modules;
}
