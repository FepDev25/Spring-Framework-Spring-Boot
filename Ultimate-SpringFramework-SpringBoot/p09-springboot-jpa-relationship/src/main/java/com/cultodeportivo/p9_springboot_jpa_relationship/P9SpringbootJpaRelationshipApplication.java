package com.cultodeportivo.p9_springboot_jpa_relationship;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import com.cultodeportivo.p9_springboot_jpa_relationship.entities.Address;
import com.cultodeportivo.p9_springboot_jpa_relationship.entities.Client;
import com.cultodeportivo.p9_springboot_jpa_relationship.entities.ClientDetails;
import com.cultodeportivo.p9_springboot_jpa_relationship.entities.Course;
import com.cultodeportivo.p9_springboot_jpa_relationship.entities.Invoice;
import com.cultodeportivo.p9_springboot_jpa_relationship.entities.Student;
import com.cultodeportivo.p9_springboot_jpa_relationship.repositories.ClientDetailsRepository;
import com.cultodeportivo.p9_springboot_jpa_relationship.repositories.ClientRepository;
import com.cultodeportivo.p9_springboot_jpa_relationship.repositories.CourseRepository;
import com.cultodeportivo.p9_springboot_jpa_relationship.repositories.InvoiceRepository;
import com.cultodeportivo.p9_springboot_jpa_relationship.repositories.StudentRepository;

@SpringBootApplication
public class P9SpringbootJpaRelationshipApplication implements CommandLineRunner {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private ClientDetailsRepository clientDetailsRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private CourseRepository courseRepository;

	public static void main(String[] args) {
		SpringApplication.run(P9SpringbootJpaRelationshipApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		manyToMany();
	}

	public void manyToMany(){
		// manyToManySave();
		manyToManyFindById();

	}

	public void manyToManyFindById(){

		Optional<Student> studentOp = studentRepository.findOne(1L);
		Optional<Student> student2Op = studentRepository.findOne(2L);

		Course course = new Course("Java", "Culto");
		Course course2 = new Course("Spring Boot", "Culto");
		Course course3 = new Course("Python", "Culto");
		Optional<Course> courseOp = courseRepository.findOne(1L);

		if ( studentOp.isPresent() && student2Op.isPresent() && courseOp.isPresent() ) {

			Student student = studentOp.get();
			Student student2 = student2Op.get();	

			Course course4 = courseOp.get();

			student.addCourse(course);
			student.addCourse(course2);
			student.addCourse(course3);

			student2.addCourse(course2);
			student2.addCourse(course3);
			student2.addCourse(course4);

			List<Student> students = (List<Student>) studentRepository.saveAll(Arrays.asList(student, student2));

			System.out.println("Guardados:");
			students.forEach(System.out::println);
		}

		// Eliminar
		Optional<Student> studentOp2 = studentRepository.findOne(1L);

		studentOp2.ifPresent(student -> {
			Optional<Course> courseOp2 = courseRepository.findOne(4L);

			courseOp2.ifPresent(courseDb -> {

				student.removeCourse(courseDb);
				studentRepository.save(student);
				System.out.println("Guardado después de eliminar: " + student);

			});
		});
	}

	public void manyToManySave(){

		Student student = new Student("Gabriel", "Santos");
		Student student2 = new Student("Flora", "Lopez");

		Course course = new Course("Java", "Culto");
		Course course2 = new Course("Spring Boot", "Culto");
		Course course3 = new Course("Python", "Culto");

		student.setCourses(Set.of(course, course2, course3));
		student2.setCourses(Set.of(course2, course3));

		studentRepository.saveAll(Arrays.asList(student, student2));

		System.out.println("Guardado: " + student);
		System.out.println("Guardado: " + student2);

	}

	

	public void oneToOne(){
		// oneToOneSave();
		// oneToOneFindById();
		// oneToOneBidirectionalSave();
		oneToOneBidirectionalFindById();
	}	

	public void oneToOneBidirectionalFindById(){

		Optional<Client> clientOp = clientRepository.findOne(1L);

		clientOp.ifPresent(client -> {
			ClientDetails clientDetails = new ClientDetails(true, 5000);
			client.setClientDetails(clientDetails);	
			clientRepository.save(client);	
			System.out.println("Guardado: " + client);
		});

		
	}

	public void oneToOneBidirectionalSave(){

		Client client = new Client("Alex", "Pereira");
		ClientDetails clientDetails = new ClientDetails(true, 5000);

		client.setClientDetails(clientDetails);

		clientRepository.save(client);	

		System.out.println("Guardado: " + client);
		
	}

	public void oneToOneFindById(){
		ClientDetails clientDetails = new ClientDetails(true, 5000);
		clientDetailsRepository.save(clientDetails);
		ClientDetails clientDetails2 = new ClientDetails(false, 100);
		clientDetailsRepository.save(clientDetails2);
		ClientDetails clientDetails3 = new ClientDetails(false, 145);
		clientDetailsRepository.save(clientDetails3);

		Optional<Client> clientOp = clientRepository.findOne(1L);
		clientOp.ifPresent(client -> {
			client.setClientDetails(clientDetails);
			clientRepository.save(client);
			System.out.println("Guardado: " + client);
		});

		Optional<Client> clientOp2 = clientRepository.findOne(2L);
		clientOp2.ifPresent(client -> {
			client.setClientDetails(clientDetails2);
			clientRepository.save(client);
			System.out.println("Guardado: " + client);
		});

		Optional<Client> clientOp3 = clientRepository.findOne(3L);
		clientOp3.ifPresent(client -> {
			client.setClientDetails(clientDetails3);
			clientRepository.save(client);
			System.out.println("Guardado: " + client);
		});

		
	}

	public void oneToOneSave(){
		ClientDetails clientDetails = new ClientDetails(true, 5000);
		clientDetailsRepository.save(clientDetails);

		Client client = new Client("Alex", "Pereira");
		client.setClientDetails(clientDetails);
		clientRepository.save(client);	

		System.out.println("Guardado: " + client);
		
	}

	@Transactional
	public void oneToMany(){
		// oneToManySave();
		// oneToManyFindById();
		// removeAddress();
		// removeAddressFindById();
		// oneToManyBidirectional();
		// oneToManyBidirectionalFindById();
		oneToMenyRemoveoneToManyBidirectionalFindById();	
	}

	@Transactional
	public void oneToMenyRemoveoneToManyBidirectionalFindById(){
		Optional<Client> clientOp = clientRepository.findOne(1L);

		if (clientOp.isPresent()) {
			Client client = clientOp.get();

			Invoice invoice1 = new Invoice("Factura No. 1", 1000L);
			Invoice invoice2 = new Invoice("Factura No. 2", 2000L);
			Invoice invoice3 = new Invoice("Factura No. 3", 3000L);

			client.addInvoice(invoice1).addInvoice(invoice2).addInvoice(invoice3);
			Client clientDb = clientRepository.save(client);

			System.out.println("Guardado 1: " + clientDb);
		}

		Optional<Client> clientOpDB = clientRepository.findOne(1L);
		clientOpDB.ifPresent(client -> {

			Optional<Invoice> invoiceOp = invoiceRepository.findById(2L);

			invoiceOp.ifPresent(invoice -> {
				client.removeInvoice(invoice);
				clientRepository.save(client);
				System.out.println("Guardado 2: " + client);
			});
		});

	}

	@Transactional
	public void oneToManyBidirectionalFindById(){
		Optional<Client> clientOp = clientRepository.findOne(1L);

		if (clientOp.isPresent()) {
			Client client = clientOp.get();

			Invoice invoice1 = new Invoice("Factura No. 1", 1000L);
			Invoice invoice2 = new Invoice("Factura No. 2", 2000L);
			Invoice invoice3 = new Invoice("Factura No. 3", 3000L);

			client.addInvoice(invoice1).addInvoice(invoice2).addInvoice(invoice3);
			clientRepository.save(client);

			System.out.println("Guardado: " + client);
		}

	}
	
	@Transactional
	public void oneToManyBidirectional(){
		Client client = new Client("Cristiano", "Ronaldo");

		Invoice invoice1 = new Invoice("Factura No. 1", 1000L);
		Invoice invoice2 = new Invoice("Factura No. 2", 2000L);
		Invoice invoice3 = new Invoice("Factura No. 3", 3000L);
	

		Set<Invoice> invoices = new HashSet<>();
		invoices.add(invoice1);
		invoices.add(invoice2);
		invoices.add(invoice3);
		client.setInvoices(invoices);

		invoice1.setClient(client);
		invoice2.setClient(client);
		invoice3.setClient(client);	

		clientRepository.save(client);

		System.out.println("Guardado: " + client);

	}

	@Transactional
	public void removeAddressFindById(){
		Optional<Client> clientOp = clientRepository.findById(2L);

		if (clientOp.isPresent()) {
			Client client = clientOp.get();

			Address address1 = new Address("Calle 7", 12);
			Address address2 = new Address("Calle 44", 11);
			Address address3 = new Address("Calle 77", 90);

			client.setAddresses(Arrays.asList(address1, address2, address3));

			Client clientDB = clientRepository.save(client);
			System.out.println("Guardado 1: " + clientDB);

			Optional<Client> clientOpRemove = clientRepository.findOne(clientDB.getId());

			clientOpRemove.ifPresent(c -> {
				c.getAddresses().remove(address2);
				clientRepository.save(c);
				System.out.println("Guardado 2: " + c);
		});
		}
	}

	@Transactional
	public void removeAddress(){
		Client client1 = new Client("Fran", "Moras");

		Address address1 = new Address("Calle 1", 78);
		Address address2 = new Address("Calle 2", 79);
		Address address3 = new Address("Calle 3", 80);

		client1.addAddress(address1, address2, address3);

		Client clienteDb = clientRepository.save(client1);
		System.out.println("Guardado 1: " + clienteDb);
		
		Optional<Client> clientOp = clientRepository.findOne(clienteDb.getId());

		clientOp.ifPresent(client -> {
			client.getAddresses().remove(address1);
			clientRepository.save(client);
			System.out.println("Guardado 2: " + client);
		});
	}

	public void oneToManyFindById(){
		Optional<Client> clientOp = clientRepository.findById(1L);

		if (clientOp.isPresent()) {
			Client client = clientOp.get();

			Address address1 = new Address("Calle 7", 12);
			Address address2 = new Address("Calle 44", 11);
			Address address3 = new Address("Calle 77", 90);

			client.setAddresses(Arrays.asList(address1, address2, address3));

			Client clientDB = clientRepository.save(client);
			System.out.println("Guardado: " + clientDB);
		}
	}

	public void oneToManySave(){
		Client client1 = new Client("Fran", "Moras");

		Address address1 = new Address("Calle 1", 78);
		Address address2 = new Address("Calle 2", 79);
		Address address3 = new Address("Calle 3", 80);

		client1.addAddress(address1, address2, address3);

		Client clienteDb = clientRepository.save(client1); // Las direcciones se guardan en cascada
		System.out.println("Guardado: " + clienteDb);

	}

	@Transactional
	public void manyToOne(){
		// manyToOneSave();
		manyToOneFindById();
	}

	public void manyToOneFindById(){
		Optional<Client> clientOp = clientRepository.findById(1L);

		if (clientOp.isPresent()) {
			Client client = clientOp.get();

			Invoice invoice1 = new Invoice("Factura No. 1", 1000L);
			invoice1.setClient(client);
			Invoice invoiceDB = invoiceRepository.save(invoice1);
			System.out.println("Factura guardada: " + invoiceDB);
		} 
	}

	public void manyToOneSave(){

		Client client1 = new Client("Roberto", "Sanchez");
		clientRepository.save(client1);

		Invoice invoice1 = new Invoice("Factura No. 1", 1000L);
		invoice1.setClient(client1);
		Invoice invoiceDB = invoiceRepository.save(invoice1);
		System.out.println("Factura guardada: " + invoiceDB);
	}

}
