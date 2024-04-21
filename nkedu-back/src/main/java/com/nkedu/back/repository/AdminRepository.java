package com.nkedu.back.repository;


import com.nkedu.back.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

	@Query("SELECT a FROM Admin a WHERE a.id = :id AND a.activated = true")
    Optional<Admin> findOneById(Long id);

}
