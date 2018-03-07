package com.open.numberManagement.restdocs.util;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;

import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;

public class ConstrainedFields {

	private final ConstraintDescriptions constraintDescriptions;

	public ConstrainedFields(Class<?> input) {
		this.constraintDescriptions = new ConstraintDescriptions(input);
	}

	public FieldDescriptor withPath(String path) {
		return fieldWithPath(path).attributes(key("maturity").value("Mandatory"));
	}

	public FieldDescriptor withPath(String path, String maturity) {
		return fieldWithPath(path).attributes(key("maturity").value(maturity));
	}		
}
