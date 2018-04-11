package com.open.numberManagement.dto.entity;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "resourceCount")
public class ResourceCountDto {

	private int resTypeId;
	private int resStatusId;
	private Long count;
	
	private ResourceTypeDto resourceType;
	private ResourceStatusDto resourceStatus;

}
