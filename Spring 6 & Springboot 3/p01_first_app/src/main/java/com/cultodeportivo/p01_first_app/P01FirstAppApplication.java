package com.cultodeportivo.p01_first_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class P01FirstAppApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(P01FirstAppApplication.class, args);
		Alien alien = context.getBean(Alien.class);
		alien.code();

		Alien alien2 = context.getBean(Alien.class);
		alien2.code();
		
	}

}
