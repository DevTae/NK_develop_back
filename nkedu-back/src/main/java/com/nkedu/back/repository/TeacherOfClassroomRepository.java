package com.nkedu.back.repository;


import com.nkedu.back.entity.TeacherOfClassroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherOfClassroomRepository extends JpaRepository<TeacherOfClassroom, Long> {


    // 선생의 ID로 찾기
    @Query("SELECT toc FROM TeacherOfClassroom toc WHERE toc.teacher.id = :teacher_id AND toc.classroom.activated=true AND toc.teacher.activated = true")
    List<TeacherOfClassroom> findAllByTeacherId(@Param("teacher_id") Long teacher_id);

    // 수업의 ID로 찾기
    @Query("SELECT toc FROM TeacherOfClassroom toc WHERE toc.classroom.id = :classroom_id AND toc.classroom.activated=true AND toc.teacher.activated = true")
    List<TeacherOfClassroom> findAllByClassroomId(@Param("classroom_id") Long classroom_id);


    // 수업과 선생님의 ID로 찾기
    @Query("SELECT toc FROM TeacherOfClassroom toc WHERE toc.classroom.id = :classroom_id AND toc.teacher.id = :teacher_id AND toc.classroom.activated=true AND toc.teacher.activated = true")
    Optional<TeacherOfClassroom> findOneByClassroomIdAndTeacherId(@Param("classroom_id") Long classroom_id, @Param("teacher_id") Long teacher_id);


}
