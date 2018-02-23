package com.open.numberManagement.dto.entity;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@XmlRootElement(name = "resource")
public class ResourceResultDto {
	
	public static final String ADD_RESULT_OK = "OK";
	public static final String ADD_RESULT_NOK = "NOK";
	
	private Integer id;
	private String name;
	private String descr;
	private Integer relResId;
	
	private String addResult = ADD_RESULT_OK;
	private String addResultMessage;
}
