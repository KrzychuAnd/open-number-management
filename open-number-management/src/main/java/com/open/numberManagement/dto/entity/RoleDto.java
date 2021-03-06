package com.open.numberManagement.dto.entity;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@XmlRootElement(name = "role")
public class RoleDto {

	private String name;
	private String descr;

	private Set<PermissionDto> permissions =  new HashSet<>();
}
