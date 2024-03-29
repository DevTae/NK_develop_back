package com.nkedu.back.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nkedu.back.entity.HomeworkOfStudent;

@Repository
public interface HomeworkOfStudentRepository extends JpaRepository<HomeworkOfStudent, Long> {

	@Query("SELECT hos FROM HomeworkOfStudent hos WHERE hos.homework.id = :homework_id and hos.student.username = :student_name")
    List<HomeworkOfStudent> findAllByHomeworkIdAndUsername(@Param("homework_id") Long homeworkId, @Param("student_name") String username);
	
	@Query("SELECT hos FROM HomeworkOfStudent hos WHERE hos.homework.id = :homework_id and hos.student.id = :student_id")
    List<HomeworkOfStudent> findAllByHomeworkIdAndStudentId(@Param("homework_id") Long homeworkId, @Param("student_id") Long studentId);
	
	@Query("SELECT hos FROM HomeworkOfStudent hos WHERE hos.homework.id = :homework_id")
	List<HomeworkOfStudent> findAllByHomeworkId(@Param("homework_id") Long homeworkId);
}
