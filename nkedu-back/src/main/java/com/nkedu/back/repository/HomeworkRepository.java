package com.nkedu.back.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nkedu.back.entity.Classroom;
import com.nkedu.back.entity.Homework;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {

	@Override
	@Query("SELECT hw FROM Homework hw WHERE hw.classroom.activated = true")
	List<Homework> findAll();
	
	@Query("SELECT hw FROM Homework hw WHERE hw.classroom.id = :classroom_id AND hw.classroom.activated = true")
	List<Homework> findAllByClassroomId(@Param("classroom_id") Long classroom_id);
	
	// 페이지 별 숙제 리스트 조회
	@Query("SELECT hw FROM Homework hw WHERE hw.classroom.id = :classroom_id AND hw.classroom.activated = true")
	Page<Homework> findAllByClassroomId(@Param("classroom_id") Long classroom_id, Pageable pageable);
	
	@Override
	@Query("SELECT hw FROM Homework hw WHERE hw.classroom.activated = true")
	Page<Homework> findAll(Pageable pageable);
}
