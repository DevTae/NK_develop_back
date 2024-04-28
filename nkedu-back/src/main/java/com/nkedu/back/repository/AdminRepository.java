package com.nkedu.back.repository;


import com.nkedu.back.entity.Admin;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

	@Query("SELECT a FROM Admin a WHERE a.id = :admin_id AND a.activated = true")
    Optional<Admin> findOneById(@Param("admin_id") Long id);

	@Override
	@Query("SELECT a FROM Admin a WHERE a.id = :admin_id AND a.activated = true")
    Optional<Admin> findById(@Param("admin_id") Long id);
	
	@Override
	@Query("SELECT a FROM Admin a WHERE a.activated = true")
    List<Admin> findAll();
	
	@Query("SELECT a FROM Admin a WHERE a.activated = true")
    Page<Admin> findAllPage();
}
