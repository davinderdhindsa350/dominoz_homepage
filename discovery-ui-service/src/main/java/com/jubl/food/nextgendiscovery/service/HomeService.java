package com.jubl.food.nextgendiscovery.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jubl.food.nextgendiscovery.model.ContentRequest;
import com.jubl.food.nextgendiscovery.model.ContentResponse;
import com.jubl.food.nextgendiscovery.model.constants.ErrorCode;
import com.jubl.food.nextgendiscovery.model.domain.PageContent;
import com.jubl.food.nextgendiscovery.model.domain.meta.theme.Hook;
import com.jubl.food.nextgendiscovery.model.domain.meta.theme.Module;
import com.jubl.food.nextgendiscovery.model.exception.BadRequestException;
import com.jubl.food.nextgendiscovery.model.properties.Discovery;
import com.jubl.food.nextgendiscovery.model.response.ParseSeoUrlResponse;
import com.jubl.food.nextgendiscovery.service.content.impl.ConstantDataLoader;
import com.jubl.food.nextgendiscovery.service.content.impl.DefaultContentLoader;
import com.jubl.food.nextgendiscovery.service.exception.ServiceException;
import com.jubl.food.nextgendiscovery.service.hooks.impl.DefaultHookRenderer;
import com.jubl.food.nextgendiscovery.service.modules.impl.DefaultModuleRenderer;

/**
 * Service level abstraction for root api's.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Service
public class HomeService {

    /**
     * static class level logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeService.class);

    /**
     * Content loader bean Content loaders loads content from external service.
     */
    @Autowired
    private DefaultContentLoader contentLoader;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    /**
     * The module renderer.
     */
    @Autowired
    private DefaultModuleRenderer moduleRenderer;

    /**
     * The hook renderer.
     */
    @Autowired
    private DefaultHookRenderer hookRenderer;

    @Autowired
    private Discovery discovery;

    @Autowired
    private ConstantDataLoader constantDataLoader;

    /**
     * ObjectMapper resolved by Spring.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Render home page.
     *
     * @param siteContext the site context
     * @param domain      the domain of the site
     * @param cache       whether to use cache or not
     * @return map of properties
     * @param contentRequest
     * @param cookies
     * @param parseSeoUrlResponse
     * @throws BadRequestException
     * @throws ServiceException
     */
    public Map<String, Object> renderHomePage(final String siteContext, final String domain, final Boolean cache,
            final ContentRequest contentRequest, final Cookie[] cookies, final ParseSeoUrlResponse parseSeoUrlResponse)
            throws BadRequestException, ServiceException {
        Map<String, Object> information = new HashMap<>();
        try {

            contentRequest.setParseSeoUrlResponse(parseSeoUrlResponse);
            // contentRequest.setCountry(response.getCountry().getIsoCode());

            final PageContent content = contentLoader.load(contentRequest, cache, cookies);

            if (content == null) {
                throw new BadRequestException(ErrorCode.EXTERNAL_SERVICE_INVALID_RESPONSE,
                        "external service didn't return any data");
            }

            if (content.getMeta() != null && content.getMeta().getTheme() != null) {
                ContentResponse contentResponse = fillContentResponseWithValues(domain, content);
                parseSeoUrlResponse.setContentResponse(contentResponse);

                information.put("htmlLang", content.getMeta().getLang().getIsoCode());

                // the body class
                if (StringUtils.isNotBlank(content.getMeta().getTheme().getBodyClass())) {
                    information.put("htmlBodyBodyClass", content.getMeta().getTheme().getBodyClass());
                }

                // render the header content
                if (content.getHtml().getHead() != null) {
                    information = addHeader(content, information);
                }

                // render the body content
                String skeleton = content.getHtml().getBody().getContent().getSkeleton();

                if (StringUtils.isBlank(skeleton)) {
                    throw new BadRequestException(ErrorCode.EXTERNAL_SERVICE_INVALID_RESPONSE,
                            "theme skeleton must not be blank");
                }

                skeleton = addHooks(content, skeleton, parseSeoUrlResponse);

                skeleton = addModule(content, skeleton, parseSeoUrlResponse);
                skeleton = skeleton.replace(discovery.getResourcePath(),
                        discovery.getResourceDomain() + "/" + discovery.getResourcePath());
                information.put("htmlBodyContent", skeleton);

                // render the body footer content
                information = addFooter(content, information);
                information.put("siteProps", fillContentResponseWithValues(domain, content));
            }
        } catch (BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceException(ErrorCode.SERVER_ERROR, "failed to render home page", ex);
        }

        return information;
    }

    /**
     * helper method to fill in values to be propagated to UI.
     *
     * @param domain  the domain of the site
     * @param content the content returned by external API call
     * @return the filled content response
     */
    private ContentResponse fillContentResponseWithValues(final String domain, final PageContent content) {
        ContentResponse contentResponse = new ContentResponse();

        contentResponse.setSite(content.getMeta().getSite().getName());
        contentResponse.setContext(content.getMeta().getSite().getContext());
        contentResponse.setUnderMaintainance(content.getMeta().getSite().getUnderMaintenance());
        contentResponse.setFavicon(content.getMeta().getSite().getFavicon());

        contentResponse.setPlatform(content.getMeta().getPlatform().getName());
        contentResponse.setPlatformId(content.getMeta().getPlatform().getId());
        contentResponse.setLang(content.getMeta().getLang().getName());
        contentResponse.setCategory(content.getMeta().getCategory().getName());
        contentResponse.setCategoryId(content.getMeta().getCategory().getId());
        contentResponse.setProfile(activeProfile);
        contentResponse.setDomain(domain);
        // contentResponse.setCurrency(response.getCurrency());
        // contentResponse.setCountry(response.getCountry());

        try {
            HashMap<String, String> propertiesMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(content.getMeta().getProps())) {
                content.getMeta().getProps().forEach(e -> propertiesMap.put(e.getName(), e.getValue()));
            }
            contentResponse.setProperties(objectMapper.writeValueAsString(propertiesMap));
        } catch (Exception ignore) {
            LOGGER.error("An error occurred while setting properties due to : {}", ignore.getMessage());
        }

        return contentResponse;
    }

    private String addHooks(final PageContent content, final String skeleton,
            final ParseSeoUrlResponse parseSeoUrlResponse) throws JsonProcessingException, BadRequestException {
        String result = skeleton;
        if (CollectionUtils.isNotEmpty(content.getMeta().getTheme().getHooks())) {
            for (Hook hook : content.getMeta().getTheme().getHooks()) {
                String rendered = hookRenderer.render(hook, content, parseSeoUrlResponse);
                if (StringUtils.isNotBlank(rendered)) {
                    result = result.replace(hook.getPlaceholder(), rendered);
                } else {
                    // if we don't find any content replace with empty string
                    result = result.replace(hook.getPlaceholder(), "");
                }
            }
        }
        return result;
    }

    private String addModule(final PageContent content, final String skeleton,
            final ParseSeoUrlResponse parseSeoUrlResponse) throws JsonProcessingException, BadRequestException {
        String result = skeleton;
        if (CollectionUtils.isNotEmpty(content.getMeta().getTheme().getModules())) {
            for (Module module : content.getMeta().getTheme().getModules()) {
                String rendered = moduleRenderer.render(module, content, parseSeoUrlResponse);
                result = result.replace(module.getPlaceholder(), rendered);
            }
        }

        return result;
    }

    private Map<String, Object> addHeader(final PageContent content, final Map<String, Object> information)
            throws JsonProcessingException {
        if (StringUtils.isNotEmpty(content.getHtml().getHead().getTitle())) {
            information.put("htmlHeadTitle", content.getHtml().getHead().getTitle());
        }

        if (content.getHtml().getHead().getMeta() != null) {
            information.put("htmlHeadMeta", content.getHtml().getHead().getMeta());
        }

        if (content.getHtml().getHead().getLinks() != null) {
            content.getHtml().getHead().getLinks()
                    .setHref(content.getHtml().getHead().getLinks().getHref().replace("", "").replace(
                            "\"" + discovery.getResourcePath(),
                            "\"" + discovery.getResourceDomain() + "/" + discovery.getResourcePath()));
            information.put("htmlHeadLinks", content.getHtml().getHead().getLinks());
        }

        if (CollectionUtils.isNotEmpty(content.getHtml().getHead().getHeaderContent())) {
            Collections.sort(content.getHtml().getHead().getHeaderContent());
            content.getHtml().getHead().getHeaderContent().stream()
                    .forEach(x -> x.setHtml(x.getHtml().replace("", "").replace("\"" + discovery.getResourcePath(),
                            "\"" + discovery.getResourceDomain() + "/" + discovery.getResourcePath())));
            information.put("htmlHeadHeaderContent", content.getHtml().getHead().getHeaderContent());
        }
        return information;
    }

    private Map<String, Object> addFooter(final PageContent content, final Map<String, Object> information) {

        if (content.getHtml().getBody().getFooter() != null && CollectionUtils.isNotEmpty(content.getHtml().getBody().getFooter().getFooterContent())) {
            Collections.sort(content.getHtml().getBody().getFooter().getFooterContent());
            content.getHtml().getBody().getFooter().getFooterContent().stream()
                    .forEach(x -> x.setHtml(x.getHtml().replace("", "").replace("\"" + discovery.getResourcePath(),
                            "\"" + discovery.getResourceDomain() + "/" + discovery.getResourcePath())));
            information.put("htmlBodyFooterFooterContent", content.getHtml().getBody().getFooter().getFooterContent());
        }
        return information;
    }

    /**
     *refresh Site Config.
     *
     */
    public void refreshSiteConfig() throws ServiceException {
        constantDataLoader.constantDataLoader();
    }

}
