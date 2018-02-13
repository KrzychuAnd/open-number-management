package com.open.numberManagement.dto.entity;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@XmlRootElement(name = "role")
public class RoleDto {

	private Integer id;
	private String name;
	private String descr;
	private String rowAddedUser;
	private Date rowAddedDttm;
	private String rowUpdatedUser;
	private Date rowUpdatedDttm;
}
