package com.nkedu.back.api;

import com.nkedu.back.dto.AdminNoticeDTO;

import java.util.List;

public interface AdminNoticeService {


    /**
     * 관리자 공지 생성
     * @param adminNoticeDTO
     * @author beom-i
     */
    public boolean createAdminNotice(AdminNoticeDTO adminNoticeDTO);

    /**
     * 관리자 공지 삭제
     * @param id
     * @author beom-i
     */
    public boolean deleteAdminNoticeById(Long id);

    /**
     * 관리자 공지 설정 (param = 바꾸고 싶은 adminNotice 의 id, 바꿀 adminNoticeDTO)
     * @param id, adminNoticeDTO
     * @return boolean
     * @author beom-i
     */
    public boolean updateAdminNotice(Long id, AdminNoticeDTO adminNoticeDTO);


    /**
     * 전체 수업 공지 조회
     * @return List<AdminNoticeDTO>
     * @author beom-i
     */
    public List<AdminNoticeDTO> getAdminNotices();

    /**
     * 특정 수업 공지 조회
     * @param adminNotice_id
     * @return AdminNoticeDTO
     * @author beom-i
     */
    public AdminNoticeDTO getAdminNotice(Long adminNotice_id);
}
