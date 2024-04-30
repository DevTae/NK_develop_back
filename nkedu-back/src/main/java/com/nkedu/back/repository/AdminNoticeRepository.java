package com.nkedu.back.repository;

import com.nkedu.back.entity.Admin;
import com.nkedu.back.entity.AdminNotice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.nkedu.back.entity.AdminNotice.AdminNoticeType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface AdminNoticeRepository extends JpaRepository<AdminNotice,Long> {

    Optional<AdminNotice> findOneById(Long id);

    @Query("SELECT not FROM AdminNotice not JOIN not.adminNoticeType ant WHERE not.admin.activated = true AND ant In :types")
    Optional<List<AdminNotice>> findByAdminNoticeTypes(@Param("types") List<AdminNoticeType> types);
    
    @Query("SELECT not FROM AdminNotice not JOIN not.adminNoticeType ant WHERE not.admin.activated = true AND ant In :types")
    Page<AdminNotice> findByAdminNoticeTypes(@Param("types") List<AdminNoticeType> types, Pageable pageable);
    
    @Override
	@Query("SELECT not FROM AdminNotice not WHERE not.admin.activated = true")
    List<AdminNotice> findAll();
    
    @Override
    @Query("SELECT not FROM AdminNotice not WHERE not.admin.activated = true")
    Page<AdminNotice> findAll(Pageable pageable);
}
