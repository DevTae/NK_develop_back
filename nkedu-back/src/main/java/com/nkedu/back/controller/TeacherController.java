package com.nkedu.back.controller;

import java.util.List;

import com.nkedu.back.dto.TeacherWithClassroomDTO;
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

import com.nkedu.back.api.TeacherService;
import com.nkedu.back.dto.PageDTO;
import com.nkedu.back.dto.StudentDTO;
import com.nkedu.back.dto.TeacherDTO;
import lombok.RequiredArgsConstructor;

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping("/teacher/list")
    public ResponseEntity<List<TeacherDTO>> getTeachers() {
        List<TeacherDTO> teacherDTOs = teacherService.getTeachers();

        if (teacherDTOs != null) {
            return new ResponseEntity<>(teacherDTOs, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    // 페이지 별 조회
 	@GetMapping("/teacher")
 	public ResponseEntity<PageDTO<TeacherDTO>> getTeachers(@RequestParam(value="page", defaultValue="0") Integer page,
                                                            @RequestParam(value="keyword", defaultValue="",required=false) String keyword) {

        PageDTO<TeacherDTO> pageDTO = teacherService.getTeachersByKeyword(page, keyword);

 		if(pageDTO != null) {
 			return new ResponseEntity<>(pageDTO, HttpStatus.OK);
 		} else {
 			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
 		}
 	}

    @GetMapping("/teacher/{username}")
    public ResponseEntity<TeacherDTO> getTeacher(@PathVariable("username") String username) {
        // 토큰 필요
        TeacherDTO teacherDTO = teacherService.getTeacherByUsername(username);
        if (teacherDTO != null) {
            return new ResponseEntity<>(teacherDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/teacher")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> createTeacher(@Validated @RequestBody TeacherDTO teacherDTO) {
        try {
            teacherService.createTeacher(teacherDTO);
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        catch (Exception e) {
            throw e;
        }
    }

    @PutMapping("/teacher/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> updateTeacher(@PathVariable("username") String username, @RequestBody TeacherDTO teacherDTO) {
        try {
            teacherService.updateTeacher(username,teacherDTO);
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        catch (Exception e) {
            throw e;
        }
    }

    /**
     * 선생님 계정 삭제 (비활성화)
     * @param username
     * @author beom-i
     */
    @DeleteMapping("/teacher/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteTeacher(@PathVariable("username") String username) {
        // 토큰 필요

        boolean result = teacherService.deleteByUsername(username);

        if (result == true) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 선생님 계정 다중 삭제
     * @param teacherDTO
     * @author beom-i
     */
    @DeleteMapping("/teacher")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteTeachers(@Validated @RequestBody TeacherDTO teacherDTO){
        return teacherService.deletesById(teacherDTO) ?
                new ResponseEntity<>(null, HttpStatus.OK) :
                new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    /**
     * 선생님들을 조회할 때, 담당하는 Classroom을 포함하여 조회할 때 사용하는 API
     * @param
     * @author beom-i
     */

    @GetMapping("/teacher/classroom")
    public ResponseEntity<List<TeacherWithClassroomDTO>> getTeachersWithClassrooms() {
        List<TeacherWithClassroomDTO> teacherWithClassroomDTOs = teacherService.getTeachersWithClassroom();

        if (teacherWithClassroomDTOs != null) {
            return new ResponseEntity<>(teacherWithClassroomDTOs, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 특정 선생님을 조회할 때, 담당하는 Classroom을 포함하여 조회할 때 사용하는 API
     * username : 조회하고 싶은 선생님 username
     * @param username
     * @author beom-i
     */
    @GetMapping("/teacher/classroom/{username}")
    public ResponseEntity<TeacherWithClassroomDTO> getTeacherWithClassroom(@PathVariable("username") String username) {
        TeacherWithClassroomDTO teacherWithClassroomDTO = teacherService.getTeacherWithClassroom(username);

        if (teacherWithClassroomDTO != null) {
            return new ResponseEntity<>(teacherWithClassroomDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}