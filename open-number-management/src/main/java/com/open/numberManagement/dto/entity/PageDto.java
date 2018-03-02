package com.open.numberManagement.dto.entity;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "page")
public class PageDto {

	private Integer size;
	private Long totalElements;
	private Integer totalPages;
	private Integer number;
	private Integer numberOfElements;

}
