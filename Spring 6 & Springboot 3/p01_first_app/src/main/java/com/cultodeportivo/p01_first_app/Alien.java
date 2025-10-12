package com.cultodeportivo.p01_first_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Alien {

    @Autowired
    Laptop laptop;

    public void code(){
        System.out.println("Alien is coding...");
        laptop.compilerCode();
    }
}
