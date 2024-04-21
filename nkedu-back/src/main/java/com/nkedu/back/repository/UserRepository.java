package com.nkedu.back.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nkedu.back.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	/*
    @EntityGraph(attributePaths="authorities")
    Optional<User> findOneWithAuthoritiesByUsername(String username);
    */
    
	@Query("SELECT u FROM User u WHERE u.username = :username AND u.activated = true")
    Optional<User> findOneByUsername(@Param("username") String username);
}