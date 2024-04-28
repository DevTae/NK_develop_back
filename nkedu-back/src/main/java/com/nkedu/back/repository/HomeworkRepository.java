package com.nkedu.back.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nkedu.back.entity.Homework;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {

	@Override
	@Query("SELECT hw FROM Homework hw WHERE hw.classroom.activated = true")
	List<Homework> findAll();
	
	@Override
	@Query("SELECT hw FROM Homework hw WHERE hw.classroom.activated = true")
	Page<Homework> findAll(Pageable pageable);
}
