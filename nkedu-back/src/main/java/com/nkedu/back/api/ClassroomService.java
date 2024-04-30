package com.nkedu.back.api;

import com.nkedu.back.dto.ClassroomDTO;
import com.nkedu.back.dto.PageDTO;
import com.nkedu.back.dto.StudentOfClassroomDTO;
import com.nkedu.back.dto.TeacherOfClassroomDTO;

import java.util.List;

public interface ClassroomService {

    /**
     * 수업 생성
     *
     * @param classroomDTO
     * @return Classroom
     * @author beom-i
     */
    public boolean createClassroom(ClassroomDTO classroomDTO);

    /**
     * 수업 삭제
     *
     * @param id
     * @author beom-i
     */
    public boolean deleteClassroomById(Long id);

    /**
     * 수업 설정 (param = 바꾸고 싶은 classroom의 id, 바꿀 classroomDTO)
     *
     * @param id, classroomDTO
     * @return boolean
     * @author beom-i
     */
    public boolean updateClassroom(Long id, ClassroomDTO classroomDTO);

    /**
     * 모든 수업 리스트 조회
     *
     * @return List<Classroom>
     * @author beom-i
     */
    public List<ClassroomDTO> getClassrooms();
    
    /**
     * 수업 리스트 페이지 별 조회
     * @param page
     * @author devtae
     * @return
     */
    public PageDTO<ClassroomDTO> getClassrooms(Integer page);

    /**
     * 수업 리스트 페이지 별 조회 + 키워드 검색 기능
     * @param page
     * @param keyword
     * @author beom-i
     * @return
     */
    public PageDTO<ClassroomDTO> getClassroomsByKeyword(Integer page, String keyword);

    /**
     * 특정 수업 조회
     *
     * @param id
     * @return ClassroomDTO
     * @author beom-i
     */
    public ClassroomDTO getClassroomById(Long id);


    /** 여기부터는 수업 - 학생, 선생 관련 CRUD API 입니다. */

    /**
     * 수업에 학생 추가
     *
     * @param classroom_id, classroomDTO
     * @return StudentOfClassroom
     * @author beom-i
     */
    public boolean createStudentOfClassroom(Long classroom_id, ClassroomDTO classroomDTO);

    /**
     * 수업에 속한 모든 학생 조회
     *
     * @param classroom_id
     * @return List<StudentOfClassroomDTO>
     * @author beom-i
     */
    public ClassroomDTO getStudentOfClassroomsByClassroomId(Long classroom_id);

    /**
     * 수업에 속한 학생 삭제
     *
     * @param classroom_id,classroomDTO
     * @return boolean
     * @author beom-i
     */
    public boolean deleteStudentOfClassroom(Long classroom_id, ClassroomDTO classroomDTO);
}