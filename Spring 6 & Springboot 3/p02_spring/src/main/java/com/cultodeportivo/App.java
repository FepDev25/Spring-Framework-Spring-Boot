package com.cultodeportivo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        System.out.println("Before creating ApplicationContext");

        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

        System.out.println("After creating ApplicationContext");
        System.out.println("Before calling getBean()");

        Alien a = (Alien) context.getBean("alien");

        System.out.println("After calling getBean()");

        a.code();

    }
}
