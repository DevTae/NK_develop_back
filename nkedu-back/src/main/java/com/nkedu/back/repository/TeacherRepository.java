package com.nkedu.back.repository;

import com.nkedu.back.entity.Teacher;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
	
	@Query("SELECT t FROM Teacher t WHERE t.username = :teachername AND t.activated = true")
	Optional<Teacher> findOneByUsername(@Param("teachername") String username);
	
	@Query("SELECT t FROM Teacher t WHERE t.id = :teacher_id AND t.activated = true")
	Optional<Teacher> findOneById(@Param("teacher_id") Long id);
	
	@Override
	@Query("SELECT t FROM Teacher t WHERE t.id = :teacher_id AND t.activated = true")
	Optional<Teacher> findById(@Param("teacher_id") Long id);
	
	@Override
	@Query("SELECT t FROM Teacher t WHERE t.activated = true")
	List<Teacher> findAll();

	@Override
	@Query("SELECT t FROM Teacher t WHERE t.activated = true")
	Page<Teacher> findAll(Pageable pageable);
}
