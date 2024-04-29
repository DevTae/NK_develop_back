package com.nkedu.back.controller;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mysql.cj.log.Log;
import com.nkedu.back.api.HomeworkOfStudentService;
import com.nkedu.back.dto.HomeworkOfStudentDTO;
import com.nkedu.back.entity.HomeworkOfStudent.Status;

import lombok.RequiredArgsConstructor;

/**
 * 숙제 제출 관련 Controller 코드 입니다.
 * @author devtae
 *
 */

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class HomeworkOfStudentController {
	
	private final HomeworkOfStudentService homeworkOfStudentService;
	
	/**
	 * 숙제 제출 리스트 조회 (선생님 채점 전용 API)
	 * @param classroomId
	 * @param homeworkId
	 * @return
	 */
	@GetMapping("/classroom/{classroom_id}/homework/{homework_id}/submit")
	public ResponseEntity<List<HomeworkOfStudentDTO>> getHomeworkOfStudent(@PathVariable("classroom_id") Long classroomId,
																  @PathVariable("homework_id") Long homeworkId,
																  @RequestParam(value="filter", required=false) String filterOption) {
		// Get Parameter 에 따른 리스트 조회 기능 제공
		Status filterStatus = null;
		
		if(!ObjectUtils.isEmpty(filterOption)) {
			switch(Status.valueOf(filterOption)) {
			case TODO:
				filterStatus = Status.TODO;
				break;
			case COMPLETE:
				filterStatus = Status.COMPLETE;
				break;
			case REJECT:
				filterStatus = Status.REJECT;
				break;
			case SUBMIT:
				filterStatus = Status.SUBMIT;
				break;
			default:
			}
		}
		
		// 권한에 따라 선생님의 경우 모든 제출을 확인할 수 있도록 하고, 학생의 경우 본인의 제출만을 확인할 수 있도록 함.
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		String roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
		
		List<HomeworkOfStudentDTO> homeworkOfStudentDTOs;
		
		if(roles.contains("ROLE_ADMIN") || roles.contains("ROLE_TEACHER")) {
			homeworkOfStudentDTOs = homeworkOfStudentService.getHomeworkOfStudents(homeworkId, filterStatus);
		} else {
			homeworkOfStudentDTOs = homeworkOfStudentService.getHomeworkOfStudents(homeworkId, username, filterStatus);
		}
		
		if (homeworkOfStudentDTOs != null) {
			return new ResponseEntity<>(homeworkOfStudentDTOs, HttpStatus.OK); 
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	/**
	 * 숙제 제출 세부 조회
	 * @param classroomId
	 * @param homeworkId
	 * @param homeworkOfStudentId
	 * @return
	 */
	@GetMapping("/classroom/{classroom_id}/homework/{homework_id}/submit/{submit_id}")
	public ResponseEntity<HomeworkOfStudentDTO> getHomeworkOfStudent(@PathVariable("classroom_id") Long classroomId,
																  @PathVariable("homework_id") Long homeworkId,
																  @PathVariable("submit_id") Long homeworkOfStudentId) {
		
		HomeworkOfStudentDTO homeworkOfStudentDTO = homeworkOfStudentService.getHomeworkOfStudent(homeworkOfStudentId);
		
		if (homeworkOfStudentDTO != null) {
			return new ResponseEntity<>(homeworkOfStudentDTO, HttpStatus.OK); 
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	/**
	 * 숙제 제출 생성
	 * @param classroomId
	 * @param homeworkId
	 * @param homeworkOfStudentDTO
	 * @return
	 */
	@PostMapping("/classroom/{classroom_id}/homework/{homework_id}/submit")
	public ResponseEntity<HomeworkOfStudentDTO> createHomeworkOfStudent(@PathVariable("classroom_id") Long classroomId,
																		@PathVariable("homework_id") Long homeworkId,
																		@RequestBody HomeworkOfStudentDTO homeworkOfStudentDTO) {
		
		homeworkOfStudentDTO.setHomeworkId(homeworkId);
		
		HomeworkOfStudentDTO homeworkOfStudentDTO_ = homeworkOfStudentService.createHomeworkOfStudent(homeworkOfStudentDTO);
		
		if (homeworkOfStudentDTO_ != null) {
			return new ResponseEntity<>(homeworkOfStudentDTO_, HttpStatus.OK); 
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * 숙제 제출 수정 (검토 및 반려 진행 가능)
	 * @param classroomId
	 * @param homeworkId
	 * @param homeworkOfStudentId
	 * @param homeworkOfStudentDTO
	 * @return
	 */
	@PutMapping("/classroom/{classroom_id}/homework/{homework_id}/submit/{submit_id}")
	public ResponseEntity<HomeworkOfStudentDTO> updateHomeworkOfStudent(@PathVariable("classroom_id") Long classroomId,
																		@PathVariable("homework_id") Long homeworkId,
																		@PathVariable("submit_id") Long homeworkOfStudentId,
																		@RequestBody HomeworkOfStudentDTO homeworkOfStudentDTO) {

		homeworkOfStudentDTO.setId(homeworkOfStudentId);
		homeworkOfStudentDTO.setHomeworkId(homeworkId);
		
		HomeworkOfStudentDTO homeworkOfStudentDTO_ = homeworkOfStudentService.updateHomeworkOfStudent(homeworkOfStudentDTO);
		
		if (homeworkOfStudentDTO_ != null) {
			return new ResponseEntity<>(homeworkOfStudentDTO_, HttpStatus.OK); 
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * 숙제 제출 삭제
	 * @param classroomId
	 * @param homeworkId
	 * @param homeworkOfStudentId
	 * @return
	 */
	@DeleteMapping("/classroom/{classroom_id}/homework/{homework_id}/submit/{submit_id}")
	public ResponseEntity<Boolean> deleteHomeworkOfStudent(@PathVariable("classroom_id") Long classroomId,
															@PathVariable("homework_id") Long homeworkId,
															@PathVariable("submit_id") Long homeworkOfStudentId) {

		boolean result = homeworkOfStudentService.deleteHomeworkOfStudent(homeworkOfStudentId);
		
		if (result == true) {
			return new ResponseEntity<>(result, HttpStatus.OK); 
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	
	/**
	 * 스탑워치 정보 가져오기 
	 * @param classroomId
	 * @param homeworkId
	 * @param homeworkOfStudentId
	 * @return
	 */
	@GetMapping("/classroom/{classroom_id}/homework/{homework_id}/submit/{submit_id}/stopwatch")
	public ResponseEntity<Double> getStopwatch(@PathVariable("classroom_id") Long classroomId,
												@PathVariable("homework_id") Long homeworkId,
												@PathVariable("submit_id") Long homeworkOfStudentId) {

		try {
			double result = homeworkOfStudentService.getStopwatch(homeworkOfStudentId);
			return new ResponseEntity<>(result, HttpStatus.OK); 
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * 스탑워치 정보 설정하기
	 * @param homeworkOfStudentDTO
	 * @param classroomId
	 * @param homeworkId
	 * @param homeworkOfStudentId
	 * @return
	 */
	@PutMapping("/classroom/{classroom_id}/homework/{homework_id}/submit/{submit_id}/stopwatch")
	public ResponseEntity<Boolean> setStopwatch(@RequestBody(required=true) HomeworkOfStudentDTO homeworkOfStudentDTO,
												@PathVariable("classroom_id") Long classroomId,
												@PathVariable("homework_id") Long homeworkId,
												@PathVariable("submit_id") Long homeworkOfStudentId) {
		boolean result = homeworkOfStudentService.setStopwatch(homeworkOfStudentId, homeworkOfStudentDTO);
		
		if(result == true) {
			return new ResponseEntity<>(result, HttpStatus.OK);				
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
}
