package com.open.numberManagement.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "exception")
public class ExceptionDto {

    private String description;

    public ExceptionDto() {
    }

    public ExceptionDto(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
