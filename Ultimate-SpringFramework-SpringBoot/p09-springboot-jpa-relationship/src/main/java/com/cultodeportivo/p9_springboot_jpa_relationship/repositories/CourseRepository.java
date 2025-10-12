package com.cultodeportivo.p9_springboot_jpa_relationship.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cultodeportivo.p9_springboot_jpa_relationship.entities.Course;

public interface CourseRepository extends CrudRepository<Course, Long> {

    @Query("SELECT c FROM Course c left join fetch c.students WHERE c.id = ?1")
    Optional<Course> findOne(Long id);
}
