/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/

package com.zymr.zvisitor.startup;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.zymr.zvisitor.service.config.AppProperties;

@Configuration
public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {

    private String[] CLASSPATH_RESOURCE_LOCATIONS = null;
            
    
    List<String> classpathResourceLocations= null;
    
    @Autowired
    AppProperties appProperties;
    
    @PostConstruct
    void init() {
    	classpathResourceLocations = new ArrayList<>();
    	classpathResourceLocations.add(ResourceUtils.CLASSPATH_URL_PREFIX+"/META-INF/resources/");
    	classpathResourceLocations.add(ResourceUtils.CLASSPATH_URL_PREFIX+"/resources/");
    	classpathResourceLocations.add(ResourceUtils.CLASSPATH_URL_PREFIX+"/static/");
    	classpathResourceLocations.add(ResourceUtils.CLASSPATH_URL_PREFIX+"/public/");
    	classpathResourceLocations.add(ResourceUtils.FILE_URL_PREFIX+"////"+appProperties.getConfig().getFileUploadsPath());    	
    	CLASSPATH_RESOURCE_LOCATIONS = new String[classpathResourceLocations.size()];
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	String[] array = classpathResourceLocations.toArray(CLASSPATH_RESOURCE_LOCATIONS);
        registry.addResourceHandler("/**")
            .addResourceLocations(array);
    }
}