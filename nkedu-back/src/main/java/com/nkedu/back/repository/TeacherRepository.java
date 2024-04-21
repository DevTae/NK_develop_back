package com.nkedu.back.repository;

import com.nkedu.back.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
	
	@Query("SELECT t FROM Teacher t WHERE t.username = :username AND t.activated = true")
	Optional<Teacher> findOneByUsername(String username);
	
	@Query("SELECT t FROM Teacher t WHERE t.id = :id AND t.activated = true")
	Optional<Teacher> findOneById(Long id);
}
