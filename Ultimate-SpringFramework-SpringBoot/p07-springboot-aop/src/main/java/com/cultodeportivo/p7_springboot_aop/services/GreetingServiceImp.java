package com.cultodeportivo.p7_springboot_aop.services;

import org.springframework.stereotype.Service;

@Service
public class GreetingServiceImp implements GreetingService {
    
    @Override
    public String sayHello(String person, String phrase) {
        String saludo = "Hello " + person + ", " + phrase;
        System.out.println(saludo);
        return saludo;
    }

    @Override
    public String sayHelloError(String person, String phrase) {
        throw new RuntimeException("Algún error.");
    }

    @Override
    public String sayHelloAround(String person, String phrase) {
        String saludo = "Hello " + person + ", " + phrase;
        System.out.println(saludo);
        return saludo;
    }
    
}
