package com.cultodeportivo.p1_springboot_web.dto;

public class ParamDto {
    private String message;

    public ParamDto() {
    }

    public ParamDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    
}
