package com.nkedu.back.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nkedu.back.api.SchoolService;
import com.nkedu.back.dto.SchoolDTO;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class SchoolController {

	private final SchoolService schoolService;
	
	// 전체 학교 리스트 조회 
	@GetMapping("/school")
	public ResponseEntity<List<SchoolDTO>>getAllSchools() {
		List<SchoolDTO> schoolDTOs = schoolService.getSchools();
		
		if (schoolDTOs != null) {
			return new ResponseEntity<>(schoolDTOs, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

		}
	}
	
	// 학교 생성 - body에 ~고등학교
	@PostMapping("/school")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> createSchool (@RequestBody SchoolDTO schoolDTO){
		return schoolService.createSchool(schoolDTO) ?
			       new ResponseEntity<>(null, HttpStatus.OK) :
			       new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}
	
	// 학교 계정 삭제 
	@DeleteMapping("/school/{schoolName}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteSchool (@PathVariable("schoolName") String schoolName){
		return schoolService.deleteBySchoolname(schoolName) ?
			       new ResponseEntity<>(null, HttpStatus.OK) :
			       new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}
}
