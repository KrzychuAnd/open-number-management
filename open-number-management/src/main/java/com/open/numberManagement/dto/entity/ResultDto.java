package com.open.numberManagement.dto.entity;

import static com.open.numberManagement.util.Constants.RESOURCE_RESULT_OK;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@XmlRootElement(name = "result")
public class ResultDto {
	
	private String code = RESOURCE_RESULT_OK;
	private String message;
}
