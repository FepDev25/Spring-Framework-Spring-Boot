package com.cultodeportivo.p8_sprinboot_jpa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cultodeportivo.p8_sprinboot_jpa.dto.PersonDto;
import com.cultodeportivo.p8_sprinboot_jpa.entities.Person;

public interface PersonRepository extends CrudRepository<Person, Long> {

    // Where

    @Query("select p from Person p where p.id IN ?1")
    List<Person> getPersonsById(List<Long> ids);

    // Subconsultas

    @Query("select p.name, length(p.name) from Person p where length(p.name) = ( select min(length(p2.name)) from Person p2 )")
    List<Object[]> shorterNames();

    @Query("select p from Person p where p.id = ( select max(p2.id) from Person p2 )")
    Optional<Person> getLastRegistration();

    // Funciones de agregación

    @Query("select count(p) from Person p")
    Long totalPersons();

    @Query("select count(p.id) from Person p where p.programmingLanguage = ?1")
    Long totalPersonsByProgrammingLanguage(String programmingLanguage);

    @Query("select max(p.id) from Person p")
    Long maxId();

    @Query("select min(p.id) from Person p")
    Long minId();

    @Query("select p.name, length(p.name) from Person p")
    List<Object[]> findNameAndLength();

    @Query("select min(length(p.name)) from Person p")
    Integer getMinLengthName();

    @Query("select max(length(p.name)) from Person p")
    Integer getMaxLengthName();

    @Query("select avg(length(p.name)) from Person p")
    Double getAvgLengthName();

    @Query("select min(p.id), max(p.id), sum(p.id), avg(length(p.name)), count(p.id) from Person p")
    Object getResumeAgregationFunction();


    // Palabra clave Order By

    @Query("select p from Person p order by p.lastName")
    List<Person> findAllOrderByLastNameMio();

    @Query("select p from Person p order by p.lastName desc")
    List<Person> findAllOrderByLastNameMioDesc();

    @Query("select p from Person p order by p.lastName, p.name")
    List<Person> findAllOrderByLastNameAndNameMio();

    List<Person> findByNameBetweenOrderByNameDesc(String c1, String c2);

    List<Person> findAllByOrderByName();

    // Palabra clave Between

    List<Person> findByIdBetween(Long id1, Long id2);

    @Query("select p from Person p where p.id between ?1 and ?2")
    List<Person> findByIdBetweenMio(Long id1, Long id2);

    List<Person> findByNameBetween(String c1, String c2);

    @Query("select p from Person p where p.name between ?1 and ?2")
    List<Person> findByNameBetweenMio(String c1, String c2);

    // Ejemplos JPQL functions

    @Query("select concat(p.name, ' ', p.lastName) from Person p")
    List<String> findAllFullNameConcat(); 

    @Query("select p.name || ' ' || p.lastName from Person p")
    List<String> findAllFullNameConcat2(); 

    @Query("select lower(concat(p.name, ' ', p.lastName)) from Person p")
    List<String> findAllFullNameLower(); 

    @Query("select upper(p.name || ' ' || p.lastName) from Person p")
    List<String> findAllFullNameUpper(); 

    @Query("select upper(p.name), upper(p.lastName), lower(p.programmingLanguage) from Person p")
    List<Object[]> findAllFullNameConcatObject();

    // Ejemplos JPQL Distinct y Count

    @Query("select p.name from Person p")
    List<String> findAllNames();

    @Query("select distinct(p.name) from Person p")
    List<String> findAllNamesDistinct();

    @Query("select distinct(p.programmingLanguage) from Person p")
    List<String> findAllProgrammingLanguageDistinct();

    @Query("select count(distinct(p.programmingLanguage)) from Person p")
    Long findAllProgrammingLanguageCount();

    // Ejemplos JPQL 2

    @Query("select p, p.programmingLanguage from Person p")
    List<Object[]> findAllMixPerson();

    @Query("select new Person(p.name, p.lastName) from Person p")
    List<Person> findAllPersonPersonalized();

    @Query("select new com.cultodeportivo.p8_sprinboot_jpa.dto.PersonDto(p.name, p.lastName) from Person p")
    List<PersonDto> findAllPersonDto();

    // Ejempplos JPQL

    @Query("select p.name from Person p where p.id = ?1")
    String getNameById(Long id); 

    @Query("select concat(p.name, ' ', p.lastName) from Person p where p.id = ?1")
    String getFullNameById(Long id); 

    @Query("select p.id from Person p where p.id = ?1")
    Long getIdById(Long id); 

    @Query("select p.name, p.programmingLanguage, p.lastName from Person p")
    List<Object[]> obtenerPersonDataFull();

    @Query("select p.name, p.lastName, p.programmingLanguage from Person p where p.id = ?1")
    Object obtenerPersonDataFull(Long id);

    
    // Devolver uno
    @Query("select p from Person p where p.id = ?1")
    Optional<Person> buscarPorId(Long id);

    @Query("select p from Person p where p.name = ?1")
    Optional<Person> buscarPorNombre(String name);

    @Query("select p from Person p where p.name like %?1%")
    Optional<Person> findOneLikeName(String name);

    Optional<Person> findByNameContaining(String name);

    // Devolver listas  
    List<Person> findByProgrammingLanguage(String programmingLanguage);

    @Query("select p from Person p where p.programmingLanguage = ?1")   
    List<Person> buscarByProgrammingLanguage(String programmingLanguage);

    List<Person> findByProgrammingLanguageAndName(String programmingLanguage, String name);

    @Query("select p from Person p where p.programmingLanguage = ?1 and p.name = ?2")
    List<Person> buscarByProgrammingLanguageAndName(String programmingLanguage, String name);

    @Query("select p.name, p.programmingLanguage from Person p")
    List<Object[]> obtenerPersonData();
    
}
