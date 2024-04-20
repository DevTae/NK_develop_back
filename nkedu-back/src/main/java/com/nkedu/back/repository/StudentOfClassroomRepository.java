package com.nkedu.back.repository;

import com.nkedu.back.entity.StudentOfClassroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentOfClassroomRepository extends JpaRepository<StudentOfClassroom, Long> {

    // 수업의 ID로 찾기
    @Query("SELECT soc FROM StudentOfClassroom soc WHERE soc.classroom.id = :classroom_id AND soc.classroom.activated = true")
    List<StudentOfClassroom> findAllByClassroomId(@Param("classroom_id") Long classroom_id);

    // 수업과 학생의 ID로 찾기
    @Query("SELECT soc FROM StudentOfClassroom soc WHERE soc.classroom.id = :classroom_id AND soc.student.id = :student_id AND soc.classroom.activated = true")
    Optional<StudentOfClassroom> findOneByClassroomIdAndStudentId(@Param("classroom_id") Long classroom_id, @Param("student_id") Long student_id);

}
