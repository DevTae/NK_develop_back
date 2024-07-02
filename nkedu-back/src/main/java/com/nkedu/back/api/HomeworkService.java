package com.nkedu.back.api;

import java.util.List;

import com.nkedu.back.dto.HomeworkDTO;
import com.nkedu.back.dto.PageDTO;
import com.nkedu.back.entity.HomeworkOfStudent.Status;

/**
 * 숙제 API 인터페이스 코드입니다.
 * @author devtae
 *
 */

public interface HomeworkService {
	
	/**
	 * Homework 리스트 반환
	 * @param class_id
	 * @return
	 */
	public List<HomeworkDTO> getHomeworks(Long classId);
	
	/**
	 * Homework 페이지별 리스트 반환
	 * @param class_id
	 * @return
	 */
	public PageDTO<HomeworkDTO> getHomeworks(Long classId, Integer page);
	
	/**
	 * Homework 리스트 변환 (학생 숙제에 대한 status 포함)
	 * @param classId
	 * @param studentId
	 * @return
	 */
	public List<HomeworkDTO> getHomeworks(Long classId, String username);
	
	/**
	 * Homework 페이지 별 리스트 반환 (학생 숙제에 대한 Status 포함)
	 * @param classId
	 * @param username
	 * @return
	 */
	public PageDTO<HomeworkDTO> getHomeworks(Long classId, String username, Integer page, Status status);
	
	/**
	 * 학생의 모든 숙제에 대한 리스트 페이지 별 반환 (학생 숙제에 대한 Status 포함)
	 * @param username
	 * @param page
	 * @param status
	 * @return
	 */
	public PageDTO<HomeworkDTO> getHomeworks(String username, Integer page, Status status);
	
	/**
	 * Homework 세부 조회 
	 * @param class_id
	 * @param homework_id
	 * @return
	 */
	public HomeworkDTO getHomework(Long classId, Long homeworkId);

	/**
	 * Homework 세부 조회 (학생 숙제에 대한 status 포함)
	 * @param classID
	 * @param homeworkId
	 * @param studentId
	 * @return
	 */
	public HomeworkDTO getHomework(Long classID, Long homeworkId, String username);
	
	/**
	 * Homework 삽입
	 * @param homeworkDTO
	 * @return
	 */
	public HomeworkDTO createHomework(HomeworkDTO homeworkDTO);
	
	/**
	 * Homework 업데이트
	 * @param homeworkDTO
	 * @return
	 */
	public HomeworkDTO updateHomework(HomeworkDTO homeworkDTO);
	
	/**
	 * Homework 삭제
	 * @param class_id
	 * @param homework_id
	 * @return
	 */
	public boolean deleteHomework(Long classId, Long homeworkId);

}
