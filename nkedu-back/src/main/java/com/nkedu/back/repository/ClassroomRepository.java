package com.nkedu.back.repository;

import com.nkedu.back.entity.ClassNotice;
import com.nkedu.back.entity.Classroom;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {


    @Query("SELECT c FROM Classroom c WHERE c.activated = true")
    List<Classroom> findAllClassroom();

    @Query("SELECT c FROM Classroom c WHERE c.id = :classroom_id AND c.activated = true")
    Optional<Classroom> findOneClassroomById(@Param("classroom_id") Long classroom_id);

    @Override
    @Query("SELECT c FROM Classroom c WHERE c.id = :classroom_id AND c.activated = true")
    Optional<Classroom> findById(@Param("classroom_id") Long id);
    
    @Override
	@Query("SELECT c FROM Classroom c WHERE c.activated = true")
    List<Classroom> findAll();
    
    @Query("SELECT c FROM Classroom c WHERE c.activated = true")
    Page<Classroom> findAllPage();
}
