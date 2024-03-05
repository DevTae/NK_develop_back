package com.nkedu.back.repository;

import com.nkedu.back.entity.ClassNotice;
import com.nkedu.back.entity.ClassNotice.ClassNoticeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassNoticeRepository extends JpaRepository<ClassNotice,Long> {

    Optional<ClassNotice> findOneById(Long id);

    // 특정 수업에 해당하는 공지 조회
    @Query("SELECT not FROM ClassNotice not WHERE not.classroom.id = :classroom_id")
    Optional<List<ClassNotice>> findAllByClassroomId(@Param("classroom_id") Long classroom_id);

    // 특정 수업에 해당하는 공지를 type에 따라 필터링하여 조회 (student or parent에 따라)
    @Query("SELECT not FROM ClassNotice not WHERE not.classroom.id = :classroom_id AND not.classNoticeType IN :types")
    Optional<List<ClassNotice>> findByClassroomIdAndClassNoticeTypes(@Param("classroom_id") Long classroom_id, @Param("types")  List<ClassNoticeType> types);


    @Query("SELECT not FROM ClassNotice not WHERE not.classroom.id = :classroom_id AND not.id = :classNotice_id")
    Optional<ClassNotice> findOneByClassroomIdAndClassNoticeId(@Param("classroom_id") Long classroom_id, @Param("classNotice_id") Long classNotice_id);

}
