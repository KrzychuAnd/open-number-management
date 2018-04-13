package com.open.numberManagement.dto.entity;

import java.util.Date;

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
	private ResourceStatusDto sourceStatus;
	private int targetStatusId;
	private ResourceStatusDto targetStatus;
	private Integer oldRelResId;
	private ResourceDto oldRelatedResource;
	private Integer newRelResId;
	private ResourceDto newRelatedResource;
	private String oldDescr;
	private String newDescr;	
	private String rowAddedUser;
	private Date rowAddedDttm;
}
