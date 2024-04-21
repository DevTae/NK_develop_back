package com.nkedu.back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nkedu.back.entity.Homework;
import com.nkedu.back.entity.HomeworkOfStudent;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {

	@Override
	@Query("SELECT hw FROM Homework hw WHERE hw.classroom.activated = true")
	List<Homework> findAll();
}
