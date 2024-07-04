package com.nkedu.back.controller;

import java.util.List;

import com.nkedu.back.exception.exception.CustomException;
import com.nkedu.back.exception.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
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

import com.nkedu.back.dto.PageDTO;
import com.nkedu.back.dto.StudentDTO;
import com.nkedu.back.api.StudentService;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class StudentController  {

	private final StudentService studentService;

	/**
	 * 모든 수업을 조회하는 controller 입니다.
	 * @return List<StudentDTO>
	 * @author beom-i
	 */
	@GetMapping("/student/list")
	public ResponseEntity<List<StudentDTO>> getStudents() {

		List<StudentDTO> studentDTOs = studentService.getStudents();

		if (studentDTOs != null) {
			return new ResponseEntity<>(studentDTOs, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * 페이지 별 학생 목록 조회
	 * @param page
	 * @param keyword 검색 키워드 (선택)
	 * @return
	 * @author devtae
	 */
	@GetMapping("/student")
	public ResponseEntity<PageDTO<StudentDTO>> getStudents(@RequestParam(value="page", defaultValue="0") Integer page,
														   @RequestParam(value="keyword", defaultValue="",required=false) String keyword) {

		PageDTO<StudentDTO> pageDTO = studentService.getStudentsByKeyword(page, keyword);

		if(pageDTO != null) {
			return new ResponseEntity<>(pageDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	// 학생 계정 생성 - body에 값 넣어서 Post
	@PostMapping("/student")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> createStudent (@Validated @RequestBody StudentDTO studentDTO){
		try {
			studentService.createStudent(studentDTO);
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
//		catch (CustomException e) {
//			throw e;
//		}
		catch (Exception e) {
			throw e;
//			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 학생 계정 삭제 (비활성화)
	 * @param username
	 * @author beom-i
	 */
	@DeleteMapping("/student/{username}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteStudent(@PathVariable("username") String username){
		return studentService.deleteByUsername(username) ?
				new ResponseEntity<>(null, HttpStatus.OK) :
				new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}

	/**
	 * 학생 계정 다중 삭제 (비활성화)
	 * @param studentDTO
	 * @author beom-i
	 */
	@DeleteMapping("/student")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteStudents(@Validated @RequestBody StudentDTO studentDTO){
		return studentService.deletesById(studentDTO) ?
				new ResponseEntity<>(null, HttpStatus.OK) :
				new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}


	// 학생 계정 설정
	@PutMapping("/student/{username}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> updateStudent (@PathVariable("username") String username, @RequestBody StudentDTO studentDTO){
		try {
			studentService.updateStudent(username, studentDTO);
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
		catch (Exception e) {
			throw e;
		}
	}

	// 특정 학생 계정 정보 조회
	@GetMapping("/student/{username}")
	public ResponseEntity<StudentDTO> getStudent(@PathVariable("username") String username) {

		StudentDTO studentDTO = studentService.getStudentByUsername(username);

		if (studentDTO != null) {
			return new ResponseEntity<>(studentDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
}
