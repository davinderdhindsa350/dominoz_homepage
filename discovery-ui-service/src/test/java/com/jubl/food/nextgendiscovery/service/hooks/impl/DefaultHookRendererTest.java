package com.jubl.food.nextgendiscovery.service.hooks.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.jubl.food.nextgendiscovery.model.domain.PageContent;
import com.jubl.food.nextgendiscovery.model.domain.meta.theme.Hook;
import com.jubl.food.nextgendiscovery.model.domain.meta.theme.Module;
import com.jubl.food.nextgendiscovery.model.response.ParseSeoUrlResponse;
import com.jubl.food.nextgendiscovery.service.modules.impl.DefaultModuleRenderer;

@RunWith(MockitoJUnitRunner.class)
public class DefaultHookRendererTest {

    @Mock
    private DefaultModuleRenderer moduleRenderer;
    
    @InjectMocks
    private DefaultHookRenderer defaultHookRenderer=new DefaultHookRenderer();
    
    @BeforeEach
     void init() {
        MockitoAnnotations.openMocks(this);
        
    }
    
    @Test
    public void testAfterPropertiesSet() throws Exception {
        assertDoesNotThrow(()-> defaultHookRenderer.afterPropertiesSet());
    }

    @Test
    public void testRender() throws Exception {
        Hook hook=new Hook();
        PageContent content=new PageContent();
        ParseSeoUrlResponse parseSeoUrlResponse=new ParseSeoUrlResponse();
        
        Module module=new Module();
        module.setPosition(1);
        Module module1=new Module();
        module1.setPosition(2);
        List<Module> lst=new ArrayList<Module>();
        lst.add(module);
        lst.add(module1);
        
        hook.setModules(lst);
        
        when(moduleRenderer.render(any(Module.class), any(PageContent.class), any(ParseSeoUrlResponse.class))).thenReturn("TestDiscovery");
        
       assertNotNull( defaultHookRenderer.render(hook, content, parseSeoUrlResponse));
    
    }

}
