package com.nkedu.back.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nkedu.back.entity.ParentOfStudent;

@Repository
public interface ParentOfStudentRepository extends JpaRepository<ParentOfStudent, Long> {

	// 부모 이름으로 찾기
	@Query("SELECT pos FROM ParentOfStudent pos WHERE pos.parent.username = :parentname AND pos.parent.activated = true AND pos.student.activated = true")
    Optional<List<ParentOfStudent>> findAllByParentname(@Param("parentname") String parentname);
	
	// 학생 이름으로 찾기
	@Query("SELECT pos FROM ParentOfStudent pos WHERE pos.student.username = :studentname AND pos.parent.activated = true AND pos.student.activated = true")
	Optional<List<ParentOfStudent>> findAllByStudentname(@Param("studentname") String studentname);
	
	// 부모 및 학생 이름으로 찾기
	@Query("SELECT pos FROM ParentOfStudent pos WHERE pos.parent.username = :parentname AND pos.student.username = :studentname AND pos.parent.activated = true AND pos.student.activated = true")
	Optional<ParentOfStudent> findOneByParentnameAndStudentname(@Param("parentname") String parentname, @Param("studentname") String studentname);
	
}
