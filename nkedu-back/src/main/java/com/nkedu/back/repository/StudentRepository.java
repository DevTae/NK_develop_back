package com.nkedu.back.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nkedu.back.entity.Student;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

	@Query("SELECT s FROM Student s WHERE s.username = :username AND s.activated = true")
    Optional<Student> findOneByUsername(String username);
	
	@Query("SELECT s FROM Student s WHERE s.id = :id AND s.activated = true")
    Optional<Student> findOneById(Long id);
}
