package com.nkedu.back.controller;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.nkedu.back.api.HomeworkService;
import com.nkedu.back.dto.HomeworkDTO;
import com.nkedu.back.dto.HomeworkOfStudentDTO;
import com.nkedu.back.dto.PageDTO;
import com.nkedu.back.entity.HomeworkOfStudent.Status;

import lombok.RequiredArgsConstructor;

/**
 * @author devtae
 * 숙제 CRUD 컨트롤러 코드입니다.
 */

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class HomeworkController {
	
	private final HomeworkService homeworkService;

	/**
	 * 해당 classroom 에 대한 모든 homework 리스트 조회
	 * @param classId
	 */
	@GetMapping("/classroom/{class_id}/homework/list")
	public ResponseEntity<List<HomeworkDTO>> getHomeworks(@PathVariable("class_id") Long classId) {
		
		// 유저에 따라 정보를 가져오도록 함.
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		List<HomeworkDTO> homeworkDTOs = homeworkService.getHomeworks(classId, username); 
		
		if (homeworkDTOs != null) {
			return new ResponseEntity<>(homeworkDTOs, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * 해당 class 에 대한 페이지 별 homework 리스트 조회
	 * @return
	 */
	@GetMapping("/classroom/{class_id}/homework")
	public ResponseEntity<PageDTO<HomeworkDTO>> getHomeworks(@PathVariable("class_id") Long classId, @RequestParam(name="page", defaultValue="0") Integer page, @RequestParam(value="filter", required=false) String filterOption) {
		
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
		
		// 유저에 따라 정보를 가져오도록 함.
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
				
		PageDTO<HomeworkDTO> pageDTO = homeworkService.getHomeworks(classId, username, page, filterStatus); 
				
		if (pageDTO != null) {
			return new ResponseEntity<>(pageDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * 학생이 속한 모든 수업의 숙제를 모두 가져오는 숙제 리스트 페이지 별 조회 API
	 * @param page
	 * @param filterOption
	 * @return
	 */
	@GetMapping("/homework")
	public ResponseEntity<PageDTO<HomeworkDTO>> getHomeworks(@RequestParam(name="page", defaultValue="0") Integer page, @RequestParam(value="filter", required=false) String filterOption) {
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
		
		// 본인의 숙제만을 확인할 수 있도록 함.
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		PageDTO<HomeworkDTO> homeworkDTOs = homeworkService.getHomeworks(username, page, filterStatus);
		
		if (homeworkDTOs != null) {
			return new ResponseEntity<>(homeworkDTOs, HttpStatus.OK); 
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * 해당 class, homework 에 대한 세부 정보 조회
	 * @param classId
	 * @param homeworkId
	 * @return
	 */
	@GetMapping("/classroom/{class_id}/homework/{homework_id}")
	public ResponseEntity<HomeworkDTO> getHomework(@PathVariable("class_id") Long classId,
										 		   @PathVariable("homework_id") Long homeworkId) {
		// 유저에 따라 정보를 가져오도록 함.
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		HomeworkDTO homeworkDTO = homeworkService.getHomework(classId, homeworkId, username); 
		
		if (homeworkDTO != null) {
			return new ResponseEntity<>(homeworkDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * 해당 class 에 homework 생성
	 * @param classId
	 * @param homeworkDTO
	 * @return
	 */
	@PostMapping("/classroom/{class_id}/homework")
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	public ResponseEntity<HomeworkDTO> createHomework(@PathVariable("class_id") Long classId,
							   				@RequestBody HomeworkDTO homeworkDTO) {
		
		try {
			HomeworkDTO homeworkDTO_ = homeworkService.createHomework(homeworkDTO);
			
			return new ResponseEntity<>(homeworkDTO_, HttpStatus.OK);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	/**
	 * 해당 class, homework 에 대한 수정
	 * @param classId
	 * @param homeworkId
	 * @param homeworkDTO
	 * @return
	 */
	@PutMapping("/classroom/{class_id}/homework/{homework_id}")
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	public ResponseEntity<HomeworkDTO> updateHomework(@PathVariable("class_id") Long classId,
							   @PathVariable("homework_id") Long homeworkId,
							   @RequestBody HomeworkDTO homeworkDTO) {
		
		try {
			
			// pk, fk 정보 입력
			homeworkDTO.setClassroomId(classId);
			homeworkDTO.setId(homeworkId);
			
			HomeworkDTO homeworkDTO_ = homeworkService.updateHomework(homeworkDTO);
			
			return new ResponseEntity<>(homeworkDTO_, HttpStatus.OK);
			
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	/**
	 * 해당 class, homework 에 대한 삭제
	 * @param classId
	 * @param homeworkId
	 * @return
	 */
	@DeleteMapping("/classroom/{class_id}/homework/{homework_id}")
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	public ResponseEntity<Boolean> deleteHomework(@PathVariable("class_id") Long classId,
											@PathVariable("homework_id") Long homeworkId) {

		boolean result = homeworkService.deleteHomework(classId, homeworkId);
				
		if (result == true) {
			return new ResponseEntity<>(null, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
}
