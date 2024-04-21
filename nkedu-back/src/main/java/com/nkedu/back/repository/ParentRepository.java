package com.nkedu.back.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nkedu.back.entity.Parent;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
	
	@Query("SELECT p FROM Parent p WHERE p.username = :username AND p.activated = true")
	Optional<Parent> findOneByUsername(String username); 
}
