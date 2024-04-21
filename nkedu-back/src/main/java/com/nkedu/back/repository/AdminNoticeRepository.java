package com.nkedu.back.repository;

import com.nkedu.back.entity.AdminNotice;
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

    @Query("SELECT not FROM AdminNotice not WHERE not.adminNoticeType IN :types AND not.admin.activated = true")
    Optional<List<AdminNotice>> findByAdminNoticeTypes(@Param("types") List<AdminNoticeType> types);



}
