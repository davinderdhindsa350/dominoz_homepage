package com.jubl.food.nextgendiscovery.service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.jubl.food.nextgendiscovery.model.constants.Constants;

/**
 * Web mvc configuration.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Configuration
@EnableWebMvc
public class WebMvcConfiguration implements WebMvcConfigurer {

    /**
     * Configure a custom view resolver with custom prefix and suffix.
     *
     * @param registry the view resolver registry
     */
    @Override
    public void configureViewResolvers(final ViewResolverRegistry registry) {
        final InternalResourceViewResolver customJspResolver = new InternalResourceViewResolver(Constants.JSP_VIEW_PREFIX,
                Constants.JSP_VIEW_SUFFIX);
        customJspResolver.setOrder(Integer.MAX_VALUE);
        customJspResolver.setViewNames("views/*");
        registry.viewResolver(customJspResolver);
    }

    /**
     *addResourceHandlers.
     */
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
        registry
                .addResourceHandler("/public/**")
                .addResourceLocations("/public/").resourceChain(true).addResolver(new PathResourceResolver());
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }
}
