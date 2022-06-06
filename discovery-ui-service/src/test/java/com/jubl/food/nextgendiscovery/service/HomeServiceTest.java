package com.jubl.food.nextgendiscovery.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.jubl.food.nextgendiscovery.model.ContentRequest;
import com.jubl.food.nextgendiscovery.model.Lang;
import com.jubl.food.nextgendiscovery.model.Meta;
import com.jubl.food.nextgendiscovery.model.Theme;
import com.jubl.food.nextgendiscovery.model.domain.PageContent;
import com.jubl.food.nextgendiscovery.model.exception.ApiException;
import com.jubl.food.nextgendiscovery.model.properties.Discovery;
import com.jubl.food.nextgendiscovery.model.response.ParseSeoUrlResponse;
import com.jubl.food.nextgendiscovery.service.content.impl.ConstantDataLoader;
import com.jubl.food.nextgendiscovery.service.content.impl.DefaultContentLoader;
import com.jubl.food.nextgendiscovery.service.hooks.impl.DefaultHookRenderer;

@RunWith(MockitoJUnitRunner.class)
public class HomeServiceTest {
    
    @InjectMocks
    private HomeService homeService=new HomeService();
    @Mock
    private DefaultContentLoader contentLoader;
    @Mock
    private DefaultHookRenderer hookRenderer;
    @Mock
    private Discovery discovery;
    @Mock
    private ConstantDataLoader constantDataLoader;

    @BeforeEach
    void init() {
       MockitoAnnotations.openMocks(this);
       discovery.setServiceBaseUrl("https://s3.dominosindia.in/jfl-discovery-service/ve1");
       when(discovery.getResourceDomain()).thenReturn("http://localhost:8021/jfl-discovery-ui/");
       when(discovery.getResourcePath()).thenReturn("public");

      
       
       
   }

    @Test
    public void testRenderHomePage() throws ApiException, URISyntaxException, IOException {
        ContentRequest request=new ContentRequest();
        request.setSite("jflcms");
        request.setPlatform("web");
        request.setCategory("/");
        request.setLang("en");
        File dataFile=new File("src/test/resources/testData.txt");
        String data = Files.readString(dataFile.toPath());
        Gson gson = new Gson();
        PageContent content1= gson.fromJson(data, PageContent.class);
        //doReturn(content).when(contentLoader).load(request, false, null);
        when(contentLoader.load(any(),anyBoolean(),any())).thenReturn(content1);
        Map<String, Object> result=homeService.renderHomePage("localhost", "localhost",false, request, null, new ParseSeoUrlResponse());
        assertEquals( result.get("htmlLang"),"en");
    }
    
    
    
    @Test
    public void testRefreshSiteConfig() {
        assertDoesNotThrow(()-> constantDataLoader.constantDataLoader() );
    }

}
