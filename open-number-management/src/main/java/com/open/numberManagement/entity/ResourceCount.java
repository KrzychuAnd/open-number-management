package com.open.numberManagement.entity;

import java.io.Serializable;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceCount implements Serializable {

	private int resTypeId;
	private int resStatusId;
	private Long count;

	public ResourceCount(HashMap<String, Object> values) {
		this.resTypeId = (Integer) values.get("type");
		this.resStatusId = (Integer) values.get("status");
		this.count = (Long) values.get("count");
	}
}
