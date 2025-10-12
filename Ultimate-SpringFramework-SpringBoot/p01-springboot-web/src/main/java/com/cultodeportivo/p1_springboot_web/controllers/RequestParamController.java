package com.cultodeportivo.p1_springboot_web.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cultodeportivo.p1_springboot_web.dto.ParamDto;
import com.cultodeportivo.p1_springboot_web.dto.ParamDtoMix;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/params")
public class RequestParamController {

    @GetMapping("/foo")
    public ParamDto foo (@RequestParam String message) {

        ParamDto paramDto = new ParamDto();
        paramDto.setMessage(message);
        
        return paramDto;
    }
    
    @GetMapping("/foo-required")
    public ParamDto fooRequired (@RequestParam(required = false, defaultValue="Default message") String message) {
        // Con name="nombre" se le puede cambiar el nombre del parámetro en la url

        ParamDto paramDto = new ParamDto();
        paramDto.setMessage(message);
        
        return paramDto;
    }

    @GetMapping("/foo-multiple")
    public ParamDtoMix fooMultiple(@RequestParam() String text, @RequestParam() Integer code) {

        ParamDtoMix paramDto = new ParamDtoMix();
        paramDto.setMessage(text);
        paramDto.setCode(code);
        return paramDto;
    }

    @GetMapping("/request")
    public ParamDtoMix var(HttpServletRequest request) {
        // Siempre recibe un tipo String

        Integer code = -1;
        try {
            code = Integer.valueOf(request.getParameter("code"));
        } catch (NumberFormatException e) {
        }

        ParamDtoMix paramDto = new ParamDtoMix();
        paramDto.setMessage(request.getParameter("message"));
        paramDto.setCode(code);

        return paramDto;
    }
    
    
       
}
