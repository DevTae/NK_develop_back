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

import com.nkedu.back.api.TeacherService;
import com.nkedu.back.dto.TeacherDTO;
import lombok.RequiredArgsConstructor;

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping("/teacher")
    public ResponseEntity<List<TeacherDTO>> getTeachers() {
        List<TeacherDTO> teacherDTOs = teacherService.getTeachers();

        if (teacherDTOs != null) {
            return new ResponseEntity<>(teacherDTOs, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/teacher/{username}")
    public ResponseEntity<TeacherDTO> getTeacher(@PathVariable("username") String username) {
        // 토큰 필요

        TeacherDTO teacherDTO = teacherService.findByUsername(username);

        if (teacherDTO != null) {
            return new ResponseEntity<>(teacherDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/teacher")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> createTeacher(@Validated @RequestBody TeacherDTO teacherDTO) {
        // 토큰 필요

        boolean result = teacherService.createTeacher(teacherDTO);

        if (result == true) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/teacher/{username}")
    public ResponseEntity<Void> updateTeacher(@PathVariable("username") String username, @RequestBody TeacherDTO teacherDTO) {
        // 토큰 필요

        boolean result = teacherService.updateTeacher(username, teacherDTO);

        if (result == true) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/teacher/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteTeacher(@PathVariable("username") String username) {
        // 토큰 필요

        boolean result = teacherService.deleteByUsername(username);

        if (result == true) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}