package com.open.numberManagement.dto;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;

public class DtoMapper {

    private MessageSource messageSource;
    
	private ModelMapper modelMapper;

    public DtoMapper(MessageSource messageSource, ModelMapper modelMapper) {
        this.messageSource = messageSource;
        this.modelMapper = modelMapper;
    }
    
    public <Source, Destination> void map(Source source, Destination destination) {
    	modelMapper.map(source, destination);
    }

    public <Source, DestinationType> DestinationType map(Source source, Class<DestinationType> type) {
    	return modelMapper.map(source, type);
    }

    public <SourceElement, DestinationType> List<DestinationType> map(List<SourceElement> source, Class<DestinationType> type) {
        return source.stream()
                .map(element -> modelMapper.map(element, type))
                .collect(Collectors.toList());
    }

    public ExceptionDto map(Exception ex, Locale locale) {
        String description = messageSource.getMessage(ex.getClass().getSimpleName(), null, locale);
        return new ExceptionDto(description);
    }

}
