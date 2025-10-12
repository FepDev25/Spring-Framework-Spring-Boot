package com.cultodeportivo.p1_springboot_web;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:values.properties", encoding="UTF-8") // cargamos el archivo de propiedades, encoding=UTF-8 
/*@PropertySources({ // cargamos varios archivos
	@PropertySource("classpath:values.properties"),
	@PropertySource("classpath:values.properties"),
})*/
public class ValuesConfig {
}
