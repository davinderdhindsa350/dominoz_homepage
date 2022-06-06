package com.jubl.food.nextgendiscovery.service.modules.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jubl.food.nextgendiscovery.model.Lang;
import com.jubl.food.nextgendiscovery.model.Meta;
import com.jubl.food.nextgendiscovery.model.Theme;
import com.jubl.food.nextgendiscovery.model.domain.PageContent;
import com.jubl.food.nextgendiscovery.model.domain.meta.theme.Module;
import com.jubl.food.nextgendiscovery.model.properties.Discovery;
import com.jubl.food.nextgendiscovery.model.response.ParseSeoUrlResponse;

@RunWith(MockitoJUnitRunner.class)
public class DefaultModuleRendererTest {

    @Spy
    private  Discovery discovery;

    @Mock
    private TemplateEngine templateEngine;
    
    @Spy
    private ObjectMapper objectMapper;
    
    @InjectMocks
    private DefaultModuleRenderer defaultModuleRenderer=new DefaultModuleRenderer();
    
    @BeforeEach
     void init() throws JsonProcessingException {
        MockitoAnnotations.openMocks(this);
        //templateEngine = mock(TemplateEngine.class);
        //discovery = spy(Discovery.class);
        //objectMapper = spy(ObjectMapper.class);
        when(discovery.getAppName()).thenReturn("TestDiscovery");
        when(discovery.getAppVersion()).thenReturn("1.0.0");
        when(templateEngine.process(eq("default/test/index"), any(Context.class))).thenReturn("test");
       // when(objectMapper.writeValueAsString("<div>test</div>")).thenReturn("<div>test</div>");
    }
    
    @Test
    public void testAfterPropertiesSet() throws Exception {
        
        assertDoesNotThrow(()-> defaultModuleRenderer.afterPropertiesSet());
    }

    @Test
    public void testRender() throws Exception {
        Module module=new Module();
        PageContent content=new PageContent();
        ParseSeoUrlResponse parseSeoUrlResponse=new ParseSeoUrlResponse();
        
        
          content.setMeta(new Meta()); 
          content.getMeta().setLang(new Lang());
          content.getMeta().setTheme(new Theme());
          content.getMeta().getLang().setIsoCode("en");
          module.setViewName("test");
          module.setPlaceholder("Placeholder");
          module.setAlias("alias");
          module.setDataType("dataType");
          module.setIndividualCache("individualCache");
          module.setPosition(1); 
          module.setData("test"); 
          assertTrue(defaultModuleRenderer.render(module,content,parseSeoUrlResponse).
          contains("test"));
          
          module.setDataType("static");
          module.setData("static");
          assertTrue(defaultModuleRenderer.render(module,content,parseSeoUrlResponse).
                  contains("static"));
             }

}
