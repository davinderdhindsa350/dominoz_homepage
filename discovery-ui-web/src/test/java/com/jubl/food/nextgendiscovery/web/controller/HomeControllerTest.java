package com.jubl.food.nextgendiscovery.web.controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import com.jubl.food.nextgendiscovery.model.ContentRequest;
import com.jubl.food.nextgendiscovery.model.exception.ApiException;
import com.jubl.food.nextgendiscovery.model.exception.BadRequestException;
import com.jubl.food.nextgendiscovery.model.properties.Discovery;
import com.jubl.food.nextgendiscovery.service.HomeService;
import com.jubl.food.nextgendiscovery.service.exception.ServiceException;
import com.jubl.food.nextgendiscovery.service.resolver.DomainSiteContextResolver;

//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(MockitoJUnitRunner.class)
public class HomeControllerTest {

   // @LocalServerPort
    //private int port;

    //@Autowired
   // private TestRestTemplate restTemplate;
    
    @Mock
    private HomeService homeService;

    @Mock
    private Discovery discovery;

    @Spy
    private Environment environment;
    
    @Spy
    private DomainSiteContextResolver domainSiteContextResolver;

    @InjectMocks
    private HomeController controller =new HomeController();

    @BeforeEach
    void init() throws BadRequestException, ServiceException {
        MockitoAnnotations.openMocks(this);
        Map<String, Object> additional =new HashMap<String, Object>();
        additional.put("htmlLang", "en");
        additional.put("siteProps","{}");
        additional.put("htmlHeadTitle", "test");
        when(homeService.renderHomePage(anyString(), anyString(), anyBoolean(), anyObject(), anyObject(), anyObject())).thenReturn(additional);
        ContentRequest request=new ContentRequest();
        request.setSite("jflcms");
        request.setPlatform("web");
        request.setCategory("/");
        request.setLang("en");
        
        when(discovery.getDefaultPlatform()).thenReturn("pwa");
        when(discovery.getDefaultCategory()).thenReturn("home");
        when(discovery.getDefaultLang()).thenReturn("en");
        when(discovery.getAppVersion()).thenReturn("2022-05-25 10:29:42");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"production","dev"});
        when(domainSiteContextResolver.resolve(any(HttpServletRequest.class))).thenReturn("jflcms");
    }
    @Test
    public void testAfterPropertiesSetInternal() throws Exception {
        assertDoesNotThrow(()-> controller.afterPropertiesSetInternal());
    }

    @Test
    public void testResolveDynamic() throws ServiceException, ApiException {
        HttpServletRequest  mockedRequest = mock(HttpServletRequest.class);
        HttpServletResponse  mockedResponse = mock(HttpServletResponse.class);
        StringBuffer s=new StringBuffer("http://localhost:8080/test/");
        when(mockedRequest.getRequestURL()).thenReturn(s);
        
        assertDoesNotThrow(()->controller.resolveDynamic(Optional.of("en"), Optional.of("pwa"),Optional.of("store"), Optional.of("64168"),false, mockedRequest, mockedResponse));
        assertDoesNotThrow(()->controller.resolveDynamic(Optional.of("en"), Optional.of("desktop"),Optional.of("/"), Optional.empty(),false, mockedRequest, mockedResponse));
        assertDoesNotThrow(()->controller.resolveDynamic(Optional.empty(), Optional.empty(),Optional.of("/"), Optional.empty(),false, mockedRequest, mockedResponse));
        s=new StringBuffer("http://localhost:8080/");
        when(mockedRequest.getRequestURL()).thenReturn(s);
        assertDoesNotThrow(()->controller.resolveDynamic(Optional.of("store"), Optional.of("64168"),Optional.empty(), Optional.empty(),false, mockedRequest, mockedResponse));
        
        // assertNotNull(this.restTemplate.getForObject("http://localhost:" + port + "/",
        //        String.class));
    }
    
    @Test
    public void testRefreshSiteConfig() throws ServiceException, ApiException {
        
        assertDoesNotThrow(()-> controller.refreshSiteConfig());
        /*
         * assertNotNull(this.restTemplate.getForObject("http://localhost:" + port +
         * "/refreshSiteConfig", String.class));
         */
    }

}
