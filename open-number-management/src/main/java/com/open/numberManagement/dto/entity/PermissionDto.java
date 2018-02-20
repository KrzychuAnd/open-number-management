package com.open.numberManagement.dto.entity;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@XmlRootElement(name = "permissions")
public class PermissionDto {

	private String name;
	private String descr;

}
