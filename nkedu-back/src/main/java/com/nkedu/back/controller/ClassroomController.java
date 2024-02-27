package com.nkedu.back.controller;

import com.nkedu.back.api.ClassroomService;
import com.nkedu.back.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ClassroomController {

    private final ClassroomService classroomService;

    /**
     * 모든 수업을 조회하는 controller 입니다.
     * @return List<ClassroomDTO>
     * @author beom-i
     */
    @GetMapping("/classroom")
    public ResponseEntity<List<ClassroomDTO>> getClassrooms() {
        List<ClassroomDTO> classroomDTOs = classroomService.getClassrooms();

        if (classroomDTOs != null) {
            return new ResponseEntity<>(classroomDTOs, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 특정 id의 수업을 조회하는 controller 입니다.
     * @param id
     * @return ClassroomDTO
     * @author beom-i
     */
    @GetMapping("/classroom/{id}")
    public ResponseEntity<ClassroomDTO> getClassroomById(@PathVariable("id") Long id) {

        ClassroomDTO classroomDTO = classroomService.getClassroomById(id);

        if (classroomDTO != null) {
            return new ResponseEntity<>(classroomDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 특정 id의 수업을 설정하는 controller 입니다.
     * @param id, classroomDTO
     *
     * @author beom-i
     */
    @PutMapping("/classroom/{id}")
    public ResponseEntity<Void> updateClassroom(@PathVariable("id") Long id, @RequestBody ClassroomDTO classroomDTO) {

        boolean result = classroomService.updateClassroom(id, classroomDTO);

        if (result == true) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 특정 수업을 생성하는 controller 입니다.
     * @param classroomDTO
     *
     * @author beom-i
     */
    @PostMapping("/classroom")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> createClassroom(@Validated @RequestBody ClassroomDTO classroomDTO) {

        boolean result = classroomService.createClassroom(classroomDTO);

        if (result == true) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 특정 수업을 삭제하는 controller 입니다.
     * @param id
     *
     * @author beom-i
     */
    @DeleteMapping("/classroom/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteClassroom(@PathVariable("id") Long id) {

        boolean result = classroomService.deleteClassroomById(id);

        if (result == true) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /** 수업-학생 수업-선생 관련 CRUD API 입니다. */

    /**
     * 수업에 학생을 추가할 수 있는 controller 입니다.
     * @param classroom_id student_name
     *
     * @author beom-i
     */
    @PostMapping("/classroom/{classroom_id}/student/{student_id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<StudentOfClassroomDTO> createStudentOfClassroom(@PathVariable("classroom_id") Long classroom_id,
                                                                    @PathVariable("student_id") Long student_id) {

        StudentOfClassroomDTO studentOfClassroomDTO = StudentOfClassroomDTO.builder()
                .classroomDTO(ClassroomDTO.builder().id(classroom_id).build())
                .studentDTO(StudentDTO.builder().id(student_id).build())
                .build();

        studentOfClassroomDTO = classroomService.createStudentOfClassroom(studentOfClassroomDTO);

        if (ObjectUtils.isNotEmpty(studentOfClassroomDTO)) {
            return new ResponseEntity<>(studentOfClassroomDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 수업에 특정 학생을 삭제하는 Controller 입니다.
     * @param classroom_id student_id
     *
     * @author beom-i
     */
    @DeleteMapping("/classroom/{classroom_id}/student/{student_id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteStudentOfClassroom(@PathVariable("classroom_id") Long classroom_id,
                                                      @PathVariable("student_id") Long student_id) {
        boolean result = classroomService.deleteStudentOfClassroom(StudentOfClassroomDTO.builder()
                .classroomDTO(ClassroomDTO.builder().id(classroom_id).build())
                .studentDTO(StudentDTO.builder().id(student_id).build())
                .build());

        if (result == true) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * 특정 수업에 모든 학생을 조회하는 Controller이다.
     * @param classroom_id
     * @return List<StudentOfClassroomDTOs>
     * @author beom-i
     */
    @GetMapping ("/classroom/{classroom_id}/student")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<StudentOfClassroomDTO>> getStudentOfClassroomsByClassroomId(@PathVariable("classroom_id") Long classroom_id) {
        List<StudentOfClassroomDTO> StudentOfClassroomDTOs = classroomService.getStudentOfClassroomsByClassroomId(classroom_id);

        if (StudentOfClassroomDTOs != null) {
            return new ResponseEntity<>(StudentOfClassroomDTOs, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 수업에 선생님을 추가할 수 있는 controller 입니다.
     * @param classroom_id teacher_id type
     *
     * @author beom-i
     */
    @PostMapping("/classroom/{classroom_id}/teacher/{teacher_id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TeacherOfClassroomDTO> createTeacherOfClassroom(@PathVariable("classroom_id") Long classroom_id,
                                                                          @PathVariable("teacher_id") Long teacher_id,
                                                                          @RequestParam("type") boolean type) {

        TeacherOfClassroomDTO teacherOfClassroomDTO = TeacherOfClassroomDTO.builder()
                .classroomDTO(ClassroomDTO.builder().id(classroom_id).build())
                .teacherDTO(TeacherDTO.builder().id(teacher_id).build())
                .type(type)
                .build();

        teacherOfClassroomDTO = classroomService.createTeacherOfClassroom(teacherOfClassroomDTO);

        if (ObjectUtils.isNotEmpty(teacherOfClassroomDTO)) {
            return new ResponseEntity<>(teacherOfClassroomDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 특정 수업에 속한 선생님의 정/부를 변경하는 Controller 입니다.
     * @param classroom_id teacher_id type
     * @return boolean
     * @author beom-i
     */
    @PutMapping("/classroom/{classroom_id}/teacher/{teacher_id}")
    public ResponseEntity<List<TeacherOfClassroomDTO>> updateTeacherOfClassrooms(@PathVariable("classroom_id") Long classroom_id,
                                                                                 @PathVariable("teacher_id") Long teacher_id,
                                                                                 @RequestParam("type") boolean type){
        boolean result = classroomService.updateTeacherOfClassroom(TeacherOfClassroomDTO.builder()
                .classroomDTO(ClassroomDTO.builder().id(classroom_id).build())
                .teacherDTO(TeacherDTO.builder().id(teacher_id).build())
                .type(type).build());

        if (result == true) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 수업에 특정 선생님을 삭제하는 Controller 입니다.
     * @param classroom_id teacher_id
     * @return boolean
     * @author beom-i
     */
    @DeleteMapping("/classroom/{classroom_id}/teacher/{teacher_id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteTeacherOfClassroom(@PathVariable("classroom_id") Long classroom_id,
                                                         @PathVariable("teacher_id") Long teacher_id) {
        boolean result = classroomService.deleteTeacherOfClassroom(TeacherOfClassroomDTO.builder()
                .classroomDTO(ClassroomDTO.builder().id(classroom_id).build())
                .teacherDTO(TeacherDTO.builder().id(teacher_id).build())
                .type(false).build());
        // 이 부분에서 type을 넣지 않으면 DTO에 type이 필수이기 때문에 오류가 난다. 삭제할 때, 어떻게 보면 정/부의 의미는 중요하지 않으니 임의의 값을 넣어서 삭제해도 되지 않을까? 더 좋은 방법이 있을까?

        if (result == true) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 특정 수업에 속한 선생님을 조회하는 Controller입니다.
     * @param classroom_id
     * @return List<TeacherOfClassroomDTOs>
     * @author beom-i
     */
    @GetMapping("/classroom/{classroom_id}/teacher")
    public ResponseEntity<List<TeacherOfClassroomDTO>> getTeacherOfClassroomsByClassroomId(@PathVariable("classroom_id") Long classroom_id){
        List<TeacherOfClassroomDTO> TeacherOfClassroomDTOs = classroomService.getTeacherOfClassroomsByClassroomId(classroom_id);

        if (TeacherOfClassroomDTOs != null) {
            return new ResponseEntity<>(TeacherOfClassroomDTOs, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
