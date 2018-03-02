package com.open.numberManagement.dto.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ResourceGenerateDto {
	
	private int resTypeId;
	private int resStatusId;
	private Integer number;
}
