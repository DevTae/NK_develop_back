package com.nkedu.back.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nkedu.back.entity.HomeworkOfStudent;

@Repository
public interface HomeworkOfStudentRepository extends JpaRepository<HomeworkOfStudent, Long> {

	@Query("SELECT hos FROM HomeworkOfStudent hos WHERE hos.homework.id = :homework_id AND hos.student.username = :student_name AND hos.student.activated = true")
    List<HomeworkOfStudent> findAllByHomeworkIdAndUsername(@Param("homework_id") Long homeworkId, @Param("student_name") String username);
	
	@Query("SELECT hos FROM HomeworkOfStudent hos WHERE hos.homework.id = :homework_id AND hos.student.id = :student_id AND hos.student.activated = true")
    List<HomeworkOfStudent> findAllByHomeworkIdAndStudentId(@Param("homework_id") Long homeworkId, @Param("student_id") Long studentId);
	
	@Query("SELECT hos FROM HomeworkOfStudent hos WHERE hos.homework.id = :homework_id AND hos.student.activated = true")
	List<HomeworkOfStudent> findAllByHomeworkId(@Param("homework_id") Long homeworkId);

	@Override
	@Query("SELECT hos FROM HomeworkOfStudent hos WHERE hos.student.activated = true")
	List<HomeworkOfStudent> findAll();
	
	@Override
	@Query("SELECT hos FROM HomeworkOfStudent hos WHERE hos.student.activated = true")
	Page<HomeworkOfStudent> findAll(Pageable pageable);
}
