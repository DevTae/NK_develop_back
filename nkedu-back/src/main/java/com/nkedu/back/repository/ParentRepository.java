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
import com.nkedu.back.entity.Parent;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
	
	@Query("SELECT p FROM Parent p WHERE p.username = :parentname AND p.activated = true")
	Optional<Parent> findOneByUsername(@Param("parentname") String username);
	
	@Override
	@Query("SELECT p FROM Parent p WHERE p.id = :parent_id AND p.activated = true")
	Optional<Parent> findById(@Param("parent_id") Long id);
	
	@Override
	@Query("SELECT p FROM Parent p WHERE p.activated = true")
	List<Parent> findAll();

	@Override
	@Query("SELECT p FROM Parent p WHERE p.activated = true")
	Page<Parent> findAll(Pageable pageable);
}
