package com.jubl.food.nextgendiscovery.service.hooks.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jubl.food.nextgendiscovery.model.domain.PageContent;
import com.jubl.food.nextgendiscovery.model.domain.meta.theme.Hook;
import com.jubl.food.nextgendiscovery.model.response.ParseSeoUrlResponse;
import com.jubl.food.nextgendiscovery.service.modules.impl.DefaultModuleRenderer;
import com.jubl.food.nextgendiscovery.service.util.ValidationUtil;
import com.jubl.food.nextgendiscovery.model.domain.meta.theme.Module;
import com.jubl.food.nextgendiscovery.model.exception.BadRequestException;

import java.util.Collections;

/**
 * Default implementation of {@link HookRenderer}.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Component
@Primary
public class DefaultHookRenderer implements InitializingBean {

    /**
     * The module renderer.
     */
    @Autowired
    private DefaultModuleRenderer moduleRenderer;

    /**
     * Callback after this bean properties are set.
     * Validates this bean
     *
     * @throws Exception if any exception occurs while validating
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        ValidationUtil.assertNotNull(moduleRenderer);
    }

    /**
     * Render the specified hook.
     *
     * @param hook    the hook to render
     * @param content the page content
     * @param parseSeoUrlResponse
     * @return the rendered string data
     * @throws BadRequestException
     * @throws JsonProcessingException
     * @throws Exception if any exception occurs while rendering
     */
    public String render(final Hook hook, final PageContent content, final ParseSeoUrlResponse parseSeoUrlResponse) throws BadRequestException, JsonProcessingException {
        if (CollectionUtils.isEmpty(hook.getModules())) {
            return null;
        }

        Collections.sort(hook.getModules());

        final StringBuilder moduleBuilder = new StringBuilder();

        for (Module module : hook.getModules()) {
            try {
                moduleBuilder.append(this.moduleRenderer.render(module, content, parseSeoUrlResponse));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        return moduleBuilder.toString();
    }
}
