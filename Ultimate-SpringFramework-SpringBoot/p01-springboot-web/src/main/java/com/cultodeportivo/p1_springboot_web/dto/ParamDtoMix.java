package com.cultodeportivo.p1_springboot_web.dto;

public class ParamDtoMix {
    private String message;
    private Integer code;

    public ParamDtoMix() {
    }

    public ParamDtoMix(String message) {
        this.message = message;
    }

    public ParamDtoMix(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    
}
