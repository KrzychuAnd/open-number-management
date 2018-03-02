package com.open.numberManagement.dto.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@XmlRootElement(name = "pageResource")
public class PageResourceDto {
	
	@XmlElement(name = "resources")
	private List<ResourceDto> resources = new ArrayList<ResourceDto>();
	
	@XmlElement(name = "page")
	private PageDto page = new PageDto();	
}
