package com.nkedu.back.api;

import com.nkedu.back.dto.ClassNoticeDTO;
import com.nkedu.back.dto.PageDTO;
import com.nkedu.back.entity.ClassNotice.ClassNoticeType;


import java.util.List;

public interface ClassNoticeService {

    /**
     * 수업 공지 생성
     * @param classroom_id, classNoticeDTO
     * @author beom-i
     */
    public boolean createClassNotice(Long classroom_id, ClassNoticeDTO classNoticeDTO);

    /**
     * 수업 공지 삭제
     * @param classroom_id, notice_id
     * @author beom-i
     */
    public boolean deleteClassNoticeById(Long classroom_id,Long notice_id);

    /**
     * 수업 공지 설정 (param = 바꾸고 싶은 classNotice의 id, 바꿀 classNoticeDTO)
     * @param classroom_id, notice_id, classNoticeDTO
     * @return boolean
     * @author beom-i
     */
    public boolean updateClassNotice(Long classroom_id, Long notice_id, ClassNoticeDTO classNoticeDTO);


    /**
     * 전체 수업 공지 조회
     * @param classroom_id
     * @return List<ClassNoticeDTO>
     * @author beom-i
     */
    public List<ClassNoticeDTO> getClassNoticesByClassroomId(Long classroom_id);
    
    /**
     * 수업 공지 페이지 별 조회
     * @param classroom_id
     * @param page
     * @param types
     * @author devtae
     */
    public PageDTO<ClassNoticeDTO> getClassNoticesByClassroomId(Long classroom_id, Integer page, List<ClassNoticeType> types);


    /**
     * 특정 수업 공지 조회
     * @param classroom_id classNotice_id
     * @return ClassNoticeDTO
     * @author beom-i
     */
    public ClassNoticeDTO getClassNoticeByClassroomIdAndClassNoticeId(Long classroom_id,Long classNotice_id);

}
