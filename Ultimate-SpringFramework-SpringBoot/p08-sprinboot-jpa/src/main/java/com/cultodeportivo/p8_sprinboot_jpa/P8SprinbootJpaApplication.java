package com.cultodeportivo.p8_sprinboot_jpa;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import com.cultodeportivo.p8_sprinboot_jpa.dto.PersonDto;
import com.cultodeportivo.p8_sprinboot_jpa.entities.Person;
import com.cultodeportivo.p8_sprinboot_jpa.repositories.PersonRepository;

@SpringBootApplication
public class P8SprinbootJpaApplication implements CommandLineRunner {

	@Autowired
	private PersonRepository personRepository;

	public static void main(String[] args) {
		SpringApplication.run(P8SprinbootJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//crearConEntrada();
		updateVersionUno();
		// list();
	}

	public void ejemploSubconsultas(){

		// Ejemplos Subconsultas
		List<Object[]> shorterNames = personRepository.shorterNames();
		System.out.println("Nombres mas cortos: ");
		shorterNames.stream().forEach(arreglo -> System.out.println(arreglo[0] + " - " + arreglo[1]));

		Optional<Person> lastRegistration = personRepository.getLastRegistration();
		if(lastRegistration.isPresent()){
			System.out.println("Ultima persona registrada: " + lastRegistration.get());
		}else{
			System.out.println("No hay personas registradas");
		}

		// Ejemplos Where In
		List<Person> people = personRepository.getPersonsById(List.of(1L, 2L, 3L));
		System.out.println("Personas con id 1, 2 o 3: ");
		people.stream().forEach(System.out::println);
	}

		public void ejemplosFuncionesAgregacion(){

		// Ejemplos
		Long totalPersons = personRepository.totalPersons();
		System.out.println("Total de personas: " + totalPersons);

		Long totalPersonsByProgrammingLanguage = personRepository.totalPersonsByProgrammingLanguage("Go");
		System.out.println("Total de personas con lenguaje de programacion Go: " + totalPersonsByProgrammingLanguage);

		Long minId = personRepository.minId();
		System.out.println("Id minimo: " + minId);

		Long maxId = personRepository.maxId();
		System.out.println("Id maximo: " + maxId);

		List<Object[]> nameAndLength = personRepository.findNameAndLength();
		System.out.println("Nombres y longitud de las personas: ");
		nameAndLength.stream().forEach(arreglo -> System.out.println(arreglo[0] + " - " + arreglo[1]));

		Integer minLengthName = personRepository.getMinLengthName();
		System.out.println("Nombre mas corto: " + minLengthName);

		Integer maxLengthName = personRepository.getMaxLengthName();
		System.out.println("Nombre mas largo: " + maxLengthName);

		Double avgLengthName = personRepository.getAvgLengthName();
		System.out.println("Promedio de longitud de nombres: " + avgLengthName);

		Object[] resumeAgregationFunction = (Object[]) personRepository.getResumeAgregationFunction();
		System.out.println("Resumen de funciones de agregacion: ");
		System.out.println("Minimo id: " + resumeAgregationFunction[0]);
		System.out.println("Maximo id: " + resumeAgregationFunction[1]);
		System.out.println("Suma id: " + resumeAgregationFunction[2]);
		System.out.println("Promedio de longitud de nombres: " + resumeAgregationFunction[3]);
		System.out.println("Cantidad de personas: " + resumeAgregationFunction[4]);
	}

	public void ejemplosPalabraClaveOrderBy(){

		// Ejemplos
		
		List<Person> people = personRepository.findAllOrderByLastNameMio();
		System.out.println("Personas ordenadas por apellido (list): ");
		people.stream().forEach(System.out::println);

		List<Person> people2 = personRepository.findAllOrderByLastNameMioDesc();
		System.out.println("Personas ordenadas por apellido (desc): ");
		people2.stream().forEach(System.out::println);

		List<Person> people3 = personRepository.findAllOrderByLastNameAndNameMio();
		System.out.println("Personas ordenadas por apellido y nombre (list): ");
		people3.stream().forEach(System.out::println);

		List<Person> people4 = personRepository.findByNameBetweenOrderByNameDesc("E", "J");
		System.out.println("Personas con nombre entre E y J (desc): ");
		people4.stream().forEach(System.out::println);

		List<Person> people5 = personRepository.findAllByOrderByName();
		System.out.println("Personas ordenadas por nombre: ");
		people5.stream().forEach(System.out::println);

	}

	public void ejemplosPalabraClaveBetween(){
		
		// Ejemplos

		List<Person> people = personRepository.findByIdBetween(1L, 5L);
		System.out.println("Personas con id entre 1 y 5: ");
		people.stream().forEach(System.out::println);

		List<Person> people2 = personRepository.findByIdBetweenMio(1L, 5L);
		System.out.println("Personas con id entre 1 y 5 (list): ");
		people2.stream().forEach(System.out::println);

		List<Person> people3 = personRepository.findByNameBetween("E", "J");
		System.out.println("Personas con nombre entre E y J: ");
		people3.stream().forEach(System.out::println);

		List<Person> people4 = personRepository.findByNameBetweenMio("E", "J");
		System.out.println("Personas con nombre entre E y J (list): ");
		people4.stream().forEach(System.out::println);

	}

	public void ejemplosJPQLFunctions(){	

		// Ejemplos

		List<String> fullNames = personRepository.findAllFullNameConcat();
		System.out.println("Nombres completos de las personas: ");
		fullNames.stream().forEach(System.out::println);

		List<String> fullNames2 = personRepository.findAllFullNameConcat2();
		System.out.println("********************Nombres completos de las personas (concat2): ********************");
		fullNames2.stream().forEach(System.out::println);

		List<String> fullNamesLower = personRepository.findAllFullNameLower();
		System.out.println("********************Nombres completos de las personas (lower): ********************");
		fullNamesLower.stream().forEach(System.out::println);

		List<String> fullNamesUpper = personRepository.findAllFullNameUpper();
		System.out.println("********************Nombres completos de las personas (upper): ********************");
		fullNamesUpper.stream().forEach(System.out::println);

		List<Object[]> fullNamesObject = personRepository.findAllFullNameConcatObject();
		System.out.println("********************Nombres completos de las personas (object): ********************");
		fullNamesObject.stream().forEach(arreglo -> System.out.println(arreglo[0] + " - " + arreglo[1] + " - " + arreglo[2]));
		
	}

	public void ejemplosJPQLDistinct(){

		// Ejemplos

		List<String> names = personRepository.findAllNames();
		System.out.println("Nombres de las personas: ");
		names.stream().forEach(System.out::println);	

		List<String> distinctNames = personRepository.findAllNamesDistinct();
		System.out.println("Nombres de las personas (distinct): ");
		distinctNames.stream().forEach(System.out::println);	

		List<String> distinctProgrammingLanguages = personRepository.findAllProgrammingLanguageDistinct();
		System.out.println("Lenguajes de programacion de las personas (distinct): ");
		distinctProgrammingLanguages.stream().forEach(System.out::println);

		Long programmingLanguageCount = personRepository.findAllProgrammingLanguageCount();
		System.out.println("Cantidad de lenguajes de programacion distintos: " + programmingLanguageCount);
	}

	@Transactional(readOnly = true)
	public void ejemplosJPQL2(){
		// Ejemplos

		List<Object[]> personRegs = personRepository.findAllMixPerson();
		System.out.println("Personas con lenguaje de programacion: ");
		personRegs.stream().forEach(reg -> {
			Person person = (Person) reg[0];
			String programmingLanguage = (String) reg[1];
			System.out.println("Persona: " + person + ", Lenguaje de programacion: " + programmingLanguage);
		});
		System.out.println();

		List<Person> personRegs2 = personRepository.findAllPersonPersonalized();
		System.out.println("Personas con nombre y apellido: ");
		personRegs2.stream().forEach(System.out::println);
		System.out.println();

		List<PersonDto> personRegs3 = personRepository.findAllPersonDto();
		System.out.println("Personas con nombre y apellido (DTO): ");
		personRegs3.stream().forEach(System.out::println);
	}


	@Transactional(readOnly = true)
	public void ejemplosJPQL(){

		Long id = 1L;

		// Ejemplos
		String name = personRepository.getNameById(2L);
		System.out.println("Nombre de la persona con id " + id + ": " + name);

		Long retornoId = personRepository.getIdById(id);
		System.out.println("Id de la persona con id " + id + ": " +  retornoId);

		String fullName = personRepository.getFullNameById(id);
		System.out.println("Nombre completo de la persona con id " + id + ": " + fullName);

		Object[] personData = (Object[]) personRepository.obtenerPersonDataFull(id);
		System.out.println("Datos de la persona con id " + id + ": " + personData[0] + " - " + personData[1] + " - " + personData[2]);

		List<Object[]> peopleData = personRepository.obtenerPersonDataFull();
		System.out.println("Datos de todas las personas: ");
		peopleData.stream().forEach(arreglo -> System.out.println(arreglo[0] + " - " + arreglo[1] + " - " + arreglo[2]));
	}

	@Transactional
	public void delete(){
		//deleteVersionUno();
		deleteVersionDos();
	}

	public void deleteVersionUno(){
		try (Scanner scanner = new Scanner(System.in)) {
			List<Person> people = (List<Person>) personRepository.findAll();
			System.out.println("Listado de personas: ");
			people.stream().forEach(System.out::println);

			System.out.println("Introduce el id de la persona a eliminar: ");
			Long id = scanner.nextLong();
			scanner.nextLine(); // Consumir el salto de línea
			// Eliminar la persona
			personRepository.deleteById(id);
			System.out.println("Persona eliminada");
		}	
	}

	public void deleteVersionDos(){
		try (Scanner scanner = new Scanner(System.in)) {
			List<Person> people = (List<Person>) personRepository.findAll();
			System.out.println("Listado de personas: ");
			people.stream().forEach(System.out::println);

			System.out.println("Introduce el id de la persona a eliminar: ");
			Long id = scanner.nextLong();
			scanner.nextLine(); // Consumir el salto de línea
			// Eliminar la persona
			Optional<Person> person = personRepository.findById(id);
			person.ifPresentOrElse(
				p -> {
					personRepository.delete(p);
					System.out.println("Persona eliminada: " + p);
				},
				() -> System.out.println("No se ha encontrado la persona")
			);
		}	
	}

	@Transactional
	public void update(){
		updateVersionDos();
	}

	public void updateVersionUno(){
		try (Scanner scanner = new Scanner(System.in)) {
			List<Person> people = (List<Person>) personRepository.findAll();
			System.out.println("Listado de personas: ");
			people.stream().forEach(System.out::println);

			System.out.println("Introduce el id de la persona a modificar: ");
			Long id = scanner.nextLong();
			scanner.nextLine(); // Consumir el salto de línea
			System.out.println("Introduce el nuevo nombre de la persona: ");
			String name = scanner.nextLine();
			System.out.println("Introduce el nuevo apellido de la persona: ");
			String lastName = scanner.nextLine();
			System.out.println("Introduce el nuevo lenguaje de programacion de la persona: ");
			String programmingLanguage = scanner.nextLine();

			Person person = personRepository.findById(id).orElseThrow();
			person.setName(name);
			person.setLastName(lastName);
			person.setProgrammingLanguage(programmingLanguage);
			// Actualizar la persona
			Person personNew = personRepository.save(person);
			System.out.println("Persona actualizada: " + personNew);
		}	
	}

	public void updateVersionDos(){
		try (Scanner scanner = new Scanner(System.in)) {
			List<Person> people = (List<Person>) personRepository.findAll();
			System.out.println("Listado de personas: ");
			people.stream().forEach(System.out::println);

			System.out.println("Introduce el id de la persona a modificar: ");
			Long id = scanner.nextLong();
			scanner.nextLine(); // Consumir el salto de línea
			System.out.println("Introduce el nuevo nombre de la persona: ");
			String name = scanner.nextLine();
			System.out.println("Introduce el nuevo apellido de la persona: ");
			String lastName = scanner.nextLine();
			System.out.println("Introduce el nuevo lenguaje de programacion de la persona: ");
			String programmingLanguage = scanner.nextLine();

			Optional<Person> person = personRepository.findById(id);
			person.ifPresentOrElse(
				p -> {
					p.setName(name);
					p.setLastName(lastName);
					p.setProgrammingLanguage(programmingLanguage);
					// Actualizar la persona
					Person personNew = personRepository.save(p);
					System.out.println("Persona actualizada: " + personNew);
				},
				() -> System.out.println("No se ha encontrado la persona")
			);
		}	
	}

	@Transactional
	public void create(){
		// Crear una persona
		crearEstatico();
		// Crear una persona con datos por consola
		crearConEntrada();
	}

	public void crearEstatico(){
		Person person1 = new Person(null, "Lionel", "Messi", "PySoccer");
		Person personNew = personRepository.save(person1);
		System.out.println("Persona creada: " + personNew);
	}

	public void crearConEntrada(){
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Introduce el nombre de la persona: ");
			String name = scanner.nextLine();
			System.out.println("Introduce el apellido de la persona: ");
			String lastName = scanner.nextLine();
			System.out.println("Introduce el lenguaje de programacion de la persona: ");
			String programmingLanguage = scanner.nextLine();
			Person person2 = new Person(null, name, lastName, programmingLanguage);

			Person personNew2 = personRepository.save(person2);
			System.out.println("Persona creada: " + personRepository.findById(personNew2.getId()).orElseThrow());
		}
	}

	public void findOne(){
		// Alternativa 1
		Person person = personRepository.findById(1L).orElseThrow();
		System.out.println("Persona encontrada: " + person);

		// Alternativa 2
		Person person2 = null;
		Optional<Person> personOptional = personRepository.findById(2L);	
		if(personOptional.isPresent()){
			person2 = personOptional.get();
		}
		System.out.println("Persona encontrada: " + person2);

		// Alternativa 3
		personRepository.findById(3L).ifPresent(System.out::println);

		// Alternativa 4
		Optional<Person> personOptional2 = personRepository.buscarPorId(4L);
		if(personOptional2.isPresent()){
			System.out.println("Persona encontrada: " + personOptional2.get());
		}else{
			System.out.println("No se ha encontrado la persona");
		}

		Optional<Person> personOptional3 = personRepository.buscarPorNombre("Felipe");
		if(personOptional3.isEmpty()){
			System.out.println("No se ha encontrado la persona");
		}else{
			System.out.println("Persona encontrada: " + personOptional3.get());
		}

		// Alternativa 5

		Optional<Person> personOptional4 = personRepository.findOneLikeName("Em");
		personOptional4.ifPresentOrElse(
			System.out::println,
			() -> System.out.println("No se ha encontrado la persona")
		);

		personOptional4 = personRepository.findOneLikeName("lia");
		personOptional4.ifPresentOrElse(
			System.out::println,
			() -> System.out.println("No se ha encontrado la persona")
		);

		Optional<Person> personOptional5 = personRepository.findByNameContaining("Jh");
		personOptional5.ifPresentOrElse(
			System.out::println,
			() -> System.out.println("No se ha encontrado la persona")
		);
	}


	public void list(){
		// Métodos JPA y personalizados

		List<Person> people = (List<Person>) personRepository.findAll();
		System.out.println("Listado de personas: ");
		people.stream().forEach(System.out::println);

		System.out.println("Personas con el lenguaje de programación Java: ");
		List<Person> peopleWithJava = personRepository.findByProgrammingLanguage("Java");
		peopleWithJava.stream().forEach(System.out::println);
		
		System.out.println("Personas con el lenguaje de programación Java con query: ");
		List<Person> peopleWithJava2 = personRepository.buscarByProgrammingLanguage("Java");
		peopleWithJava2.stream().forEach(System.out::println);

		
		System.out.println("Persona con el lenguaje de programación Java y el nombre Felipe: ");
		List<Person> peopleWithJava3 = personRepository.findByProgrammingLanguageAndName("Java", "Felipe");
		peopleWithJava3.stream().forEach(System.out::println);


		System.out.println("Persona con el lenguaje de programación Java y el nombre Felipe con query: ");
		List<Person> peopleWithJava4 = personRepository.buscarByProgrammingLanguageAndName("Java", "Felipe");
		peopleWithJava4.stream().forEach(System.out::println);

		// Obtener data de entidad

		System.out.println("Person data:");
		List<Object[]> peopleData = personRepository.obtenerPersonData();
		peopleData.stream().forEach(arreglo -> System.out.println(arreglo[0] + " - " + arreglo[1]));
	}

}
