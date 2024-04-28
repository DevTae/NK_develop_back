package com.nkedu.back.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nkedu.back.entity.Student;
import com.nkedu.back.entity.StudentOfClassroom;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

	@Query("SELECT s FROM Student s WHERE s.username = :studentname AND s.activated = true")
    Optional<Student> findOneByUsername(@Param("studentname") String username);
	
	@Query("SELECT s FROM Student s WHERE s.id = :student_id AND s.activated = true")
    Optional<Student> findOneById(@Param("student_id") Long id);
	
	@Override
	@Query("SELECT s FROM Student s WHERE s.id = :student_id AND s.activated = true")
	Optional<Student> findById(@Param("student_id") Long id);
	
	@Override
	@Query("SELECT s FROM Student s WHERE s.activated = true")
	List<Student> findAll();
	
	@Query("SELECT s FROM Student s WHERE s.activated = true")
	Page<Student> findAllPage();
}
