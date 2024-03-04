package com.nkedu.back.api;

import java.util.List;

import com.nkedu.back.dto.HomeworkOfStudentDTO;

/**
 * 숙제 제출 API 인터페이스 코드입니다.
 * @author devtae
 *
 */

public interface HomeworkOfStudentService {
	
	/**
	 * 숙제 제출 세부 조회
	 * @param homeworkId
	 * @return
	 */
	public HomeworkOfStudentDTO getHomeworkOfStudent(Long homeworkId);
	
	/**
	 * 숙제 제출 리스트 조회
	 * @return
	 */
	public List<HomeworkOfStudentDTO> getHomeworkOfStudents();
	
	/**
	 * 숙제 제출 생성
	 * @param homeworkOfStudentDTO
	 * @return
	 */
	public HomeworkOfStudentDTO createHomeworkOfStudent(HomeworkOfStudentDTO homeworkOfStudentDTO);
	
	/**
	 * 숙제 제출 삭제
	 * @param homeworkId
	 * @return
	 */
	public boolean deleteHomeworkOfStudent(Long homeworkId);
	
	/**
	 * 숙제 제출 수정
	 * @param homeworkId
	 * @param homeworkOfStudentDTO
	 * @return
	 */
	public HomeworkOfStudentDTO updateHomeworkOfStudent(Long homeworkId, HomeworkOfStudentDTO homeworkOfStudentDTO);
	
}
