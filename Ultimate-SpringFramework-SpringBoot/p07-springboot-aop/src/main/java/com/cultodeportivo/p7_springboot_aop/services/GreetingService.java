package com.cultodeportivo.p7_springboot_aop.services;

public interface  GreetingService {

    String sayHello(String person, String phrase);
    String sayHelloError(String person, String phrase);
    String sayHelloAround(String person, String phrase);
}
