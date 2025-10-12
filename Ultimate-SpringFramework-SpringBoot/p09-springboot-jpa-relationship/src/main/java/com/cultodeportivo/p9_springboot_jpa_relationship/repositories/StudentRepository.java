package com.cultodeportivo.p9_springboot_jpa_relationship.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cultodeportivo.p9_springboot_jpa_relationship.entities.Student;

public interface StudentRepository extends CrudRepository<Student, Long> {

    @Query("SELECT s FROM Student s left join fetch s.courses WHERE s.id = ?1")
    Optional<Student> findOne(Long id);
}
