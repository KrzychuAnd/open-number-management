package com.open.numberManagement.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.open.numberManagement.dto.DtoMapper;

@Configuration
public class Beans {
	
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasename("errors");
        return messageSource;
    }

    @Bean
    public DtoMapper dtoMapper(MessageSource messageSource) {
        return new DtoMapper(messageSource);
    }	
    
    @Bean
    public ObjectMapper objectMapper() {
    	return new ObjectMapper();
    }
}
