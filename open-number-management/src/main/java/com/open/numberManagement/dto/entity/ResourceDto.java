package com.open.numberManagement.dto.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
@XmlRootElement(name = "resource")
public class ResourceDto {
	
	private Integer id;
	private String href;
	@NotBlank
	private String name;
	@Min(value=1, message="The value must be positive")
	private int resTypeId;
	@Min(value=1, message="The value must be positive")
	private int resStatusId;
	
	private String descr;
	private Integer relResId;
	private String rowAddedUser;
	private Date rowAddedDttm;
	private String rowUpdatedUser;
	
	@XmlElement(name = "resourceHistories")
	private Set<ResourceHistoryDto> resourceHistories = new HashSet<>();
	
	public ResourceDto(String name, int resTypeId, int resStatusId, String descr, Integer relResId ) {
		this.name = name;
		this.resTypeId = resTypeId;
		this.resStatusId = resStatusId;
		this.descr = descr;
		this.relResId = relResId;
	}
}
