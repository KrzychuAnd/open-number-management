package com.open.numberManagement.dto.entity;

import static com.open.numberManagement.util.Constants.RESOURCE_RESULT_OK;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@XmlRootElement(name = "resource")
public class ResourceResultDto {
	
	private Integer id;
	private String href;
	private String name;
	private String descr;
	private Integer relResId;
	
	private String addResult = RESOURCE_RESULT_OK;
	private String addResultMessage;
}
