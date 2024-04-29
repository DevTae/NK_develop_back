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
import com.nkedu.back.entity.HomeworkOfStudent.Status;

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
	
	
	// 수업 및 사용자에 대한 페이지 별 숙제 리스트 조회 (필터링 기능 포함)
	@Query("SELECT DISTINCT h FROM Homework h " +
		       "JOIN StudentOfClassroom soc ON soc.classroom.id = h.classroom.id " +
		       "LEFT JOIN HomeworkOfStudent hos ON hos.homework.id = h.id AND hos.student.id = soc.student.id " +
		       "WHERE h.classroom.activated = true " +
		       "AND soc.classroom.id = :classroom_id AND soc.student.username = :username " +
		       "AND ((hos.status IS NULL AND :status = 'TODO') OR hos.status = :status)")
	Page<Homework> findAllByClassroomIdAndUsernameAndStatus(@Param("classroom_id") Long classroom_id, 
												   			@Param("username") String username, 
												   			@Param("status") Status status, 
												   			Pageable pageable);
	
	// 수업 및 사용자에 대한 페이지 별 숙제 리스트 조회
	@Query("SELECT DISTINCT h FROM Homework h " +
		       "JOIN StudentOfClassroom soc ON soc.classroom.id = h.classroom.id " +
		       "LEFT JOIN HomeworkOfStudent hos ON hos.homework.id = h.id AND hos.student.id = soc.student.id " +
		       "WHERE h.classroom.activated = true " + 
		       "AND soc.classroom.id = :classroom_id AND soc.student.username = :username")
	Page<Homework> findAllByClassroomIdAndUsername(@Param("classroom_id") Long classroom_id, 
												   @Param("username") String username, 
												   Pageable pageable);
	
	// 사용자에 대한 페이지 별 숙제 리스트 조회 (필터링 기능 포함)
	@Query("SELECT DISTINCT h FROM Homework h " +
		       "JOIN StudentOfClassroom soc ON soc.classroom.id = h.classroom.id " +
		       "LEFT JOIN HomeworkOfStudent hos ON hos.homework.id = h.id AND hos.student.id = soc.student.id " +
		       "WHERE h.classroom.activated = true " +
		       "AND soc.student.username = :username " +
		       "AND ((hos.status IS NULL AND :status = 'TODO') OR hos.status = :status)")
	Page<Homework> findAllByUsernameAndStatus(@Param("username") String username, @Param("status") Status status, Pageable pageable);
	
	// 사용자에 대한 페이지 별 숙제 리스트 조회
	@Query("SELECT DISTINCT h FROM Homework h " +
		       "JOIN StudentOfClassroom soc ON soc.classroom.id = h.classroom.id " +
		       "LEFT JOIN HomeworkOfStudent hos ON hos.homework.id = h.id AND hos.student.id = soc.student.id " +
		       "WHERE h.classroom.activated = true " +
		       "AND soc.student.username = :username")
	Page<Homework> findAllByUsername(@Param("username") String username, Pageable pageable);
	
}
