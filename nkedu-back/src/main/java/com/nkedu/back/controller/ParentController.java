package com.nkedu.back.controller;

import java.util.List;

import com.nkedu.back.dto.*;
import org.apache.commons.lang3.ObjectUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nkedu.back.api.ParentService;
import com.nkedu.back.entity.Parent;
import com.nkedu.back.entity.ParentOfStudent;
import com.nkedu.back.entity.Student;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

//@Slf4j
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ParentController {
	private final ParentService parentService;
	
	@GetMapping("/parent/list")
	public ResponseEntity<List<ParentDTO>> getParents() {
		List<ParentDTO> parentDTOs = parentService.getParents();
		
		if (parentDTOs != null) {
			return new ResponseEntity<>(parentDTOs, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); 
		}
	}
	
	// 페이지 별 부모님 조회 + 검색기능
	@GetMapping("/parent")
	public ResponseEntity<PageDTO<ParentDTO>> getParents(@RequestParam(value="page", defaultValue="1") Integer page,
														 @RequestParam(value="keyword", defaultValue="",required=false) String keyword) {
		
		PageDTO<ParentDTO> pageDTO = parentService.getParentsByKeyword(page,keyword);
		
		if(pageDTO != null) {
			return new ResponseEntity<>(pageDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/parent/{username}")
	public ResponseEntity<ParentDTO> getParent(@PathVariable("username") String username) {
		// 본인 혹은 관리자만 열람 가능하도록 토큰 필요

		ParentDTO parentDTO = parentService.getParentByUsername(username);
		
		if (parentDTO != null) {
			return new ResponseEntity<>(parentDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/parent/{username}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> updateParent(@PathVariable("username") String username, @RequestBody ParentDTO parentDTO) {

		try {

			boolean result = parentService.updateParent(username, parentDTO);
			
			return new ResponseEntity<>(null, HttpStatus.OK);
			
		} catch (Exception e) {
			throw e;
		}
		
	}

	@PostMapping("/parent")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> createParent(@Validated @RequestBody ParentDTO parentDTO) {

		try {
			boolean result = parentService.createParent(parentDTO);
			
			return new ResponseEntity<>(null, HttpStatus.OK);
			
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 부모님 계정 삭제 (비활성화)
	 * @param username
	 * @author DevTae
	 */
	@DeleteMapping("/parent/{username}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteParent(@PathVariable("username") String username) {
	
		boolean result = parentService.deleteByUsername(username);
		
		if (result == true) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * 부모님 계정 다중 삭제 (비활성화)
	 * @param parentDTO
	 * @author beom-i
	 */
	@DeleteMapping("/parent")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteTeachers(@Validated @RequestBody ParentDTO parentDTO){
		return parentService.deletesById(parentDTO) ?
				new ResponseEntity<>(null, HttpStatus.OK) :
				new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/parent/{parentname}/student")
	public ResponseEntity<List<ParentOfStudentDTO>> getParentOfStudentsByParentname(@PathVariable("parentname") String parentname) {
		
		// 본인 확인 로직 필요
		
		List<ParentOfStudentDTO> parentOfStudentDTOs = parentService.getParentOfStudentsByParentname(parentname);
				
		if (parentOfStudentDTOs != null) {
			return new ResponseEntity<>(parentOfStudentDTOs, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/parent/{parentname}/student/{studentname}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ParentOfStudentDTO> createParentOfStudent(@PathVariable("parentname") String parentname,
																	@PathVariable("studentname") String studentname,
																	@Valid @RequestBody RelationshipDTO relationshipDTO) {
		
		try {
			
			ParentOfStudentDTO parentOfStudentDTO = ParentOfStudentDTO.builder()
					.parentDTO(ParentDTO.builder().username(parentname).build())
					.studentDTO(StudentDTO.builder().username(studentname).build())
					.relationship(relationshipDTO.getRelationship())
					.build();

			parentOfStudentDTO = parentService.createParentOfStudent(parentOfStudentDTO);
			
			return new ResponseEntity<>(parentOfStudentDTO, HttpStatus.OK);
			
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	@DeleteMapping("/parent/{parentname}/student/{studentname}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteParentOfStudent(@PathVariable("parentname") String parentname,
												      @PathVariable("studentname") String studentname) {
		boolean result = parentService.deleteParentOfStudent(ParentOfStudentDTO.builder()
															 .parentDTO(ParentDTO.builder().username(parentname).build())
															 .studentDTO(StudentDTO.builder().username(studentname).build())
															 .build());
							
		if (result == true) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
