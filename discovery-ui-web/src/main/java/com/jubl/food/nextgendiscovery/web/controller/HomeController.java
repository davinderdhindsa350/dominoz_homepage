package com.jubl.food.nextgendiscovery.web.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.jubl.food.nextgendiscovery.model.ContentRequest;
import com.jubl.food.nextgendiscovery.model.constants.Constants;
import com.jubl.food.nextgendiscovery.model.constants.ErrorCode;
import com.jubl.food.nextgendiscovery.model.exception.ApiException;
import com.jubl.food.nextgendiscovery.model.properties.Discovery;
import com.jubl.food.nextgendiscovery.model.response.ParseSeoUrlResponse;
import com.jubl.food.nextgendiscovery.service.HomeService;
import com.jubl.food.nextgendiscovery.service.resolver.DomainSiteContextResolver;
import com.jubl.food.nextgendiscovery.service.util.RequestUtil;
import com.jubl.food.nextgendiscovery.service.util.ValidationUtil;

/**
 * The home discovery.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@RestController
@RequestMapping("/")
@Order(Ordered.LOWEST_PRECEDENCE)
public class HomeController extends AbstractApiController {

    /**
     * Production profile name.
     */
    private static final String PRODUCTION_PROFILE_NAME = "production";

    /**
     * spring resolved home service instance.
     */
    @Autowired
    private HomeService homeService;

    /**
     * Application level properties resolved by spring.
     */
    @Autowired
    private Discovery discovery;

    /**
     * Resolve site context from request.
     */
    @Autowired
    private DomainSiteContextResolver domainSiteContextResolver;

    /**
     * The spring application environment.
     */
    @Autowired
    private Environment environment;

    /**
     * Callback after properties are set Validates this bean.
     *
     * @throws Exception if validation failed
     */
    // @Override
    protected void afterPropertiesSetInternal() {
        ValidationUtil.assertNotNull(homeService, discovery, environment);
    }

    /**
     * ugly checks on url's, try to find some other way for this.
     *
     * @param request the http servlet request
     * @return the base url string
     * @throws URISyntaxException
     */
    private String extractAssetsBaseUrl(final HttpServletRequest request) throws URISyntaxException {
        URI baseURI = new URIBuilder(request.getRequestURL().toString()).build();

        String path = baseURI.getPath();

        final URIBuilder uriBuilder = new URIBuilder(request.getRequestURL().toString());

        if (Arrays.asList(environment.getActiveProfiles()).contains(PRODUCTION_PROFILE_NAME)) {
            uriBuilder.setHost(RequestUtil.getMediaPath(request));
        }

        if (StringUtils.isNotBlank(path)) {
            if (path.startsWith("/")) {
                path = path.substring(1);
            }

            if (path.endsWith("/")) {
                path = StringUtils.substringBeforeLast(path, "/");
            }

            String[] split = path.split("/");

            return uriBuilder.setPath(split[0]).setScheme(null).build().toString();
        }

        return new URIBuilder(baseURI).setScheme(null).toString();
    }

    /**
     * ugly checks on url's, try to find some other way for this.
     * @param isMediaHandlingRequired
     * @param request                 the http servlet request
     * @return the base url string
     * @throws URISyntaxException
     */
    private String extractAssetsBaseUrl(final HttpServletRequest request, final boolean isMediaHandlingRequired)
            throws URISyntaxException {
        URI baseURI = new URIBuilder(request.getRequestURL().toString()).build();

        String path = baseURI.getPath();

        final URIBuilder uriBuilder = new URIBuilder(request.getRequestURL().toString());

        if (isMediaHandlingRequired
                && Arrays.asList(environment.getActiveProfiles()).contains(PRODUCTION_PROFILE_NAME)) {
            return uriBuilder.setHost(RequestUtil.getMediaPath(request)).setPath(Constants.MEDIA_SUFFIX).setScheme(null)
                    .build().toString();
        }

        if (StringUtils.isNotBlank(path)) {
            if (path.startsWith("/")) {
                path = path.substring(1);
            }

            if (path.endsWith("/")) {
                path = StringUtils.substringBeforeLast(path, "/");
            }

            String[] split = path.split("/");

            return uriBuilder.setPath(split[0]).setScheme(null).build().toString();
        }

        return new URIBuilder(baseURI).setScheme(null).toString();
    }

    /**
     * refresh Site Config.
     * @return Response
     */
    @GetMapping(value = { "refreshSiteConfig" })
    public ResponseEntity<?> refreshSiteConfig() {
        try {
            homeService.refreshSiteConfig();
            return ResponseEntity.ok("SUCCESS");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }

    }

    /**
     * The default home page.
     *
     * @param request  the http servlet request
     * @param response the http servlet response
     * @param platform the optional platform
     * @param lang     the optional language
     * @param category
     * @param storeId
     * @param cache    the optional cache
     * @return the view name
     */
    @GetMapping(value = { "/", "/{category}", "/{lang}/{platform}", "/{category}/{storeId}",
            "/{lang}/{platform}/{category}", "/{lang}/{platform}/{category}/{storeId}" })
    public ModelAndView resolveDynamic(final @PathVariable(value = "lang") Optional<String> lang,
            final @PathVariable(value = "platform") Optional<String> platform,
            final @PathVariable(value = "category") Optional<String> category,
            final @PathVariable(value = "storeId") Optional<String> storeId,
            final @RequestParam(value = "cache", required = false) Boolean cache, final HttpServletRequest request,
            final HttpServletResponse response) throws ApiException {
        final ModelAndView model = new ModelAndView();
        ParseSeoUrlResponse parseSeoUrlResponse = new ParseSeoUrlResponse();

        try {
            // resolve the site context from the domain here
            String host = domainSiteContextResolver.resolve(request);
            Optional<String> categoryLocal = category;
            Optional<String> langLocal = lang;
            Optional<String> storeIdLocal = storeId;
            Optional<String> platformLocal = platform;
            if (platform.isPresent() && !Constants.PLATFORMS.contains(platform.get())) {
                categoryLocal = langLocal;
                storeIdLocal = platform;
                langLocal = Optional.empty();
                platformLocal = Optional.empty();
            }
            final ContentRequest contentRequest = new ContentRequest(host,
                    platformLocal.orElse(discovery.getDefaultPlatform()),
                    categoryLocal.orElse(discovery.getDefaultCategory()), langLocal.orElse(discovery.getDefaultLang()));
            if (storeIdLocal.isPresent()) {
                contentRequest.setStoreId(storeIdLocal.get());
            }

            parseSeoUrlResponse.setSiteDomain(extractAssetsBaseUrl(request, false));
            parseSeoUrlResponse.setSiteBaseUrl(extractBaseUrl(request, false));
            parseSeoUrlResponse.setRequestPathWithoutPage(extractBaseUrl(request, true));
            String siteDomain = RequestUtil.getCookieDomain(request);
            String baseUrl = extractAssetsBaseUrl(request);
            contentRequest.setDomain(baseUrl);
            final Map<String, Object> additional = homeService.renderHomePage(host, siteDomain, cache, contentRequest,
                    request.getCookies(), parseSeoUrlResponse);

            if (MapUtils.isNotEmpty(additional)) {
                for (Map.Entry<String, Object> entry : additional.entrySet()) {
                    model.addObject(entry.getKey(), entry.getValue());
                }
            }

            model.addObject("baseUrl", baseUrl);

            StringBuilder publicPath = new StringBuilder("public");
            if (Arrays.asList(environment.getActiveProfiles()).contains(PRODUCTION_PROFILE_NAME)) {
                publicPath.append(String.format("/%s", host));
            }
            model.addObject("publicPath", publicPath);
            SimpleDateFormat formatter6 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            model.addObject("version", formatter6.parse(discovery.getAppVersion()).getTime() / 1000);
            model.setViewName(Constants.HOME_VIEW_NAME);
            return model;
        } catch (Exception ex) {
            throw new ApiException(ErrorCode.SERVER_ERROR, "unknown error while rendering home page", ex);
        }
    }

    private String extractBaseUrl(final HttpServletRequest request, final boolean isPathRequired)
            throws URISyntaxException {

        final URIBuilder uriBuilder = new URIBuilder(request.getRequestURL().toString());
        if (isPathRequired) {
            return uriBuilder.setScheme(null).build().toString();
        } else {
            return uriBuilder.setPath("").setScheme(null).build().toString();
        }

    }
}
