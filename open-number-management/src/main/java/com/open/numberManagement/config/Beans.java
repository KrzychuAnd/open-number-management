package com.open.numberManagement.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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
        messageSource.setBasenames("i18/errors");
        return messageSource;
    }

    @Bean
    public ModelMapper modelMapper() {
    	return new ModelMapper();
    }
    
    @Bean
    public DtoMapper dtoMapper(MessageSource messageSource, ModelMapper modelMapper) {
    	modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return new DtoMapper(messageSource, modelMapper);
    }	
    
    @Bean
    public ObjectMapper objectMapper() {
    	return new ObjectMapper();
    }
}
