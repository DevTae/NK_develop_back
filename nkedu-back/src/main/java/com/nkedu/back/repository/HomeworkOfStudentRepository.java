package com.nkedu.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nkedu.back.entity.HomeworkOfStudent;

@Repository
public interface HomeworkOfStudentRepository extends JpaRepository<HomeworkOfStudent, Long> {

}
