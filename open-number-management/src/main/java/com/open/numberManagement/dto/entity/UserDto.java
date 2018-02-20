package com.open.numberManagement.dto.entity;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@XmlRootElement(name = "user")
public class UserDto {

	private String login;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private Integer roleId;
	private char locked;
	private String rowAddedUser;
	private Date rowAddedDttm;
	private String rowUpdateUser;
	private Date rowUpdateDttm;
	
	private RoleDto role;
	
}
