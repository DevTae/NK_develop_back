package com.nkedu.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nkedu.back.entity.School;

import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, String> {

    @Query("SELECT s FROM School s WHERE s.schoolName = :schoolName")
    Optional<School> findBySchoolName(@Param("schoolName") String schoolName);


}
