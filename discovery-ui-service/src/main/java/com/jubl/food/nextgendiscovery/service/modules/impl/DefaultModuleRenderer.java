package com.jubl.food.nextgendiscovery.service.modules.impl;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jubl.food.nextgendiscovery.model.constants.Constants;
import com.jubl.food.nextgendiscovery.model.constants.ErrorCode;
import com.jubl.food.nextgendiscovery.model.domain.PageContent;
import com.jubl.food.nextgendiscovery.model.domain.meta.theme.Module;
import com.jubl.food.nextgendiscovery.model.exception.BadRequestException;
import com.jubl.food.nextgendiscovery.model.properties.Discovery;
import com.jubl.food.nextgendiscovery.model.response.ParseSeoUrlResponse;
import com.jubl.food.nextgendiscovery.service.util.ValidationUtil;
/**
 * Default implementation of {@link ModuleRenderer}.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Primary
@Component
public class DefaultModuleRenderer implements InitializingBean {

    /**
    *
    */
   private static final Logger LOGGER = LoggerFactory.getLogger(DefaultModuleRenderer.class);

    /**
     * The thymeleaf template engine.
     */
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * Application level properties resolved by spring.
     */
    @Autowired
    private Discovery discovery;

    /**
     * The object mapper.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Callback after this bean properties are set.
     * Validates this bean
     *
     * @throws Exception if any exception occurs while validating
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        ValidationUtil.assertNotNull(templateEngine);
    }

    /**
     * Render the module.
     *
     * @param module  the module to render
     * @param content the page content returned from service
     * @return the rendered string content
     * @throws BadRequestException
     * @throws JsonProcessingException
     * @throws Exception               if any error occurs while rendering
     * @param parseSeoUrlResponse
     * @return
     * @throws BadRequestException
     * @throws JsonProcessingException
     */
    public String render(final Module module, final PageContent content, final ParseSeoUrlResponse parseSeoUrlResponse) throws BadRequestException, JsonProcessingException {
        validateCheckContent(module, content);
        String language = content.getMeta().getLang().getIsoCode();
        if (StringUtils.isBlank(language)) {
            language = discovery.getDefaultLang();
        }

        String theme = content.getMeta().getTheme().getDirectory();

        if (StringUtils.isBlank(theme)) {
            theme = Constants.DEFAULT_THEME;
        }



        final Context context = new Context(Locale.forLanguageTag(language));

        // Setting locale from url
//        final Context context;
//        String langFromUrl = StringUtils.substringBetween(parseSeoUrlResponse.getRequestPathWithoutPage(), "web/", "/home");
//        if(langFromUrl != null && !langFromUrl.isEmpty() ){
//            context = new Context(Locale.forLanguageTag(langFromUrl));
//        }else{
//            context = new Context(Locale.forLanguageTag(language));
//        }

        // check user is logged in or not

        context.setVariable("parseSeoResponse", parseSeoUrlResponse);
        context.setVariable("name", module.getPlaceholder());
        context.setVariable("alias", module.getAlias());
        context.setVariable("viewName", module.getViewName());
        context.setVariable("dataType", module.getDataType());
        context.setVariable("individualCache", module.getIndividualCache());
        context.setVariable("position", module.getPosition());
        context.setVariable("moduleProps", objectMapper.writeValueAsString(module.getModuleProps()));
        if (parseSeoUrlResponse != null && parseSeoUrlResponse.getContentResponse() !=  null) {
            context.setVariable("siteProperties", parseSeoUrlResponse.getContentResponse());
        }
        if ("STATIC".equalsIgnoreCase(module.getDataType())) {
            return module.getData().toString();
        }


        if (module.getData() instanceof String) {
            context.setVariable("data", module.getData());

            try {
                JSONParser parser = new JSONParser();
                Object dataJsonObj = parser.parse(module.getData().toString());
                context.setVariable("resultData", dataJsonObj);

            } catch (Exception e) {
                LOGGER.error("Exception while parsing data= {} || {} || {}", module.getName(), module.getData(), e);
            }

        } else {
            String json = objectMapper.writeValueAsString(module.getData());
            context.setVariable("data", json);
        }

        context.setVariable("queryData", objectMapper.writeValueAsString(module.getQueryData()));

        String templateName = String.format("%s/%s", theme, module.getViewName());
        templateName = String.format("%s/%s", templateName, "index");
        return this.templateEngine.process(templateName, context);
    }

    private void validateCheckContent(final Module module, final PageContent content) throws BadRequestException {
        if (content == null || content.getMeta() == null || content.getMeta().getLang() == null) {
            throw new BadRequestException(ErrorCode.EXTERNAL_SERVICE_INVALID_RESPONSE, "language meta must not be null");
        } else if (content.getMeta() == null || content.getMeta().getTheme() == null) {
            throw new BadRequestException(ErrorCode.EXTERNAL_SERVICE_INVALID_RESPONSE, "theme meta must not be null");
        }
        if (StringUtils.isBlank(module.getViewName())) {
            throw new BadRequestException(ErrorCode.EXTERNAL_SERVICE_INVALID_RESPONSE, "view name must not be blank");
        }
    }
}
