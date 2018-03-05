package com.open.numberManagement.dto.entity;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@XmlRootElement(name = "resourceHistory")
public class ResourceHistoryDto {

	private Integer sourceStatusId;
	private int targetStatusId;
	private Integer oldRelResId;
	private Integer newRelResId;	
	private String oldDescr;
	private String newDescr;	

}
