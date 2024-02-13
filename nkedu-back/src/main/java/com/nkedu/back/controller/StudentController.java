package com.nkedu.back.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nkedu.back.dto.StudentDTO;
import com.nkedu.back.api.StudentService;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class StudentController  {
	
	private final StudentService studentService;
	
	// 전체 학생 계정 리스트 조회 
	@GetMapping("/student")
	public ResponseEntity<List<StudentDTO>> getStudents() {
		List<StudentDTO> studentDTOs = studentService.getStudents();
		
		if (studentDTOs != null) {
			return new ResponseEntity<>(studentDTOs, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}	
	}
	
	// 학생 계정 생성 - body에 값 넣어서 Post
	@PostMapping("/student")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> createStudent (@Validated @RequestBody StudentDTO studentDTO){
		return studentService.createStudent(studentDTO) ?
			       new ResponseEntity<>(null, HttpStatus.OK) :
			       new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);	
	}
	
	// 학생 계정 삭제 
	@DeleteMapping("/student/{username}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteStudent(@PathVariable("username") String username){
		return studentService.deleteByUsername(username) ?
			       new ResponseEntity<>(null, HttpStatus.OK) :
			       new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}
	
	
	// 학생 계정 설정 
	@PutMapping("/student/{username}")
	public ResponseEntity<Void> updateStudent (@PathVariable("username") String username, @RequestBody StudentDTO studentDTO){
		return studentService.updateStudent(username,studentDTO) ?
			       new ResponseEntity<>(null, HttpStatus.OK) :
			       new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}
	
	// 특정 학생 계정 정보 조회 
	@GetMapping("/student/{username}")
	public ResponseEntity<StudentDTO> getStudent(@PathVariable("username") String username) {
		
		StudentDTO studentDTO = studentService.findByUsername(username);
		
		if (studentDTO != null) {
			return new ResponseEntity<>(studentDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	

	// 학생 계정 세부정보 조회 
}
