package com.nkedu.back.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.nkedu.back.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    
}
