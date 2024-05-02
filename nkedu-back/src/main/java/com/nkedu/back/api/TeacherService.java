package com.nkedu.back.api;

import java.util.List;

import com.nkedu.back.dto.TeacherWithClassroomDTO;
import com.nkedu.back.dto.PageDTO;
import com.nkedu.back.dto.TeacherDTO;

public interface TeacherService {

	/**
	 * 선생님 계정 생성 (토큰이 필요함)
	 * @param teacherDTO
	 * @return Teacher
	 */
	public boolean createTeacher(TeacherDTO teacherDTO);

	/**
	 * 선생님 계정 삭제 (토큰이 필요함)
	 * @param username
	 */
	public boolean deleteByUsername(String username);

	/**
	 * 선생님 계정 설정 (토큰이 필요함)
	 * @param username, teacherDTO
	 * @return
	 */
	public boolean updateTeacher(String username, TeacherDTO teacherDTO);

	/**
	 * 선생님 계정 리스트 조회
	 * @return List<Teacher>
	 */
	public List<TeacherDTO> getTeachers();
	


	/**
	 * 선생님 계정 페이지 별 조회 + 키워드 검색 기능
	 * @param page
	 * @param keyword
	 * @author devtae
	 * @return
	 */
	public PageDTO<TeacherDTO> getTeachersByKeyword(Integer page,String keyword);

	/**
	 * 선생님 계정 정보 조회
	 * @param username
	 * @return Teacher
	 */
	public TeacherDTO getTeacherByUsername(String username);


	/**
	 * 선생님 모든 계정 리스트 조회 with Classroom
	 * @return List<Teacher>
	 */
	public List<TeacherWithClassroomDTO> getTeachersWithClassroom();

	/**
	 * 선생님 특정 계정 정보 조회 with Classroom
	 * @param username
	 * @return Teacher
	 */
	public TeacherWithClassroomDTO getTeacherWithClassroom(String username);
}