package com.open.numberManagement.dto.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "resources")
public class ResourcesDto {
	
	private int resTypeId;
	private int resStatusId;

	@XmlElement(name = "result")
	private ResultDto result = new ResultDto();
	
	@XmlElement(name = "resource")
	private List<ResourceResultDto> resources;

}
