package com.nkedu.back.controller;

import com.nkedu.back.api.ClassroomService;
import com.nkedu.back.api.ClassNoticeService;
import com.nkedu.back.dto.*;
import com.nkedu.back.entity.ClassNotice.ClassNoticeType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ClassroomController {

    private final ClassroomService classroomService;
    private final ClassNoticeService classNoticeService;


    /**
     * 모든 수업을 조회하는 controller 입니다.
     * @return List<ClassroomDTO>
     * @author beom-i
     */
    @GetMapping("/classroom/list")
    public ResponseEntity<List<ClassroomDTO>> getClassrooms() {
        List<ClassroomDTO> classroomDTOs = classroomService.getClassrooms();

        if (classroomDTOs != null) {
            return new ResponseEntity<>(classroomDTOs, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * 페이지 별 수업 목록 조회
     * @param page
     * @param keyword 검색 키워드 (선택)
     * @return
     * @author devtae
     */
    @GetMapping("/classroom")
    public ResponseEntity<PageDTO<ClassroomDTO>> getClassrooms(@RequestParam(value="page", defaultValue="0") Integer page,
                                                               @RequestParam(value="keyword", required=false) String keyword) {

        PageDTO<ClassroomDTO> pageDTO;
        // 키워드가 존재하는 경우
        if (keyword != null && !keyword.isEmpty()) {
            pageDTO = classroomService.getClassroomsByKeyword(page, keyword);
        }
        // 키워드가 존재하지 않거나 빈 문자열인 경우
        else {
            pageDTO = classroomService.getClassrooms(page);
        }

    	if(pageDTO != null) {
    		return new ResponseEntity<>(pageDTO, HttpStatus.OK);
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

    /** 수업-학생 관련 CRUD API 입니다. */

    /**
     * 수업에 학생을 추가할 수 있는 controller 입니다.
     * @param classroom_id student_name
     *
     * @author beom-i
     */
    @PostMapping("/classroom/{classroom_id}/student")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> createStudentOfClassroom(@PathVariable("classroom_id") Long classroom_id,
                                                                          @Validated @RequestBody ClassroomDTO classroomDTO) {


        boolean result = classroomService.createStudentOfClassroom(classroom_id,classroomDTO);

        if (result == true) {
            return new ResponseEntity<>(null, HttpStatus.OK);
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
    @DeleteMapping("/classroom/{classroom_id}/student")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteStudentOfClassroom(@PathVariable("classroom_id") Long classroom_id,
                                                         @Validated @RequestBody ClassroomDTO classroomDTO) {
        boolean result = classroomService.deleteStudentOfClassroom(classroom_id,classroomDTO);

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
    public ResponseEntity<ClassroomDTO> getStudentOfClassroomsByClassroomId(@PathVariable("classroom_id") Long classroom_id) {
        ClassroomDTO classroomDTO = classroomService.getStudentOfClassroomsByClassroomId(classroom_id);

        if (classroomDTO != null) {
            return new ResponseEntity<>(classroomDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /** 수업 - 공지 관련 API 입니다. */

    /**
     * 전체 수업 공지를 조회하는 Controller입니다.
     * @param classroom_id type
     * @return List<ClassNoticeDTO>
     * @author beom-i
     */
    @GetMapping("/classroom/{classroom_id}/class-notice/list")
    public ResponseEntity<List<ClassNoticeDTO>> getClassNoticesByClassroom(@PathVariable("classroom_id") Long classroom_id) {

        List<ClassNoticeDTO> classNoticeDTOs = classNoticeService.getClassNoticesByClassroomId(classroom_id);

        if (classNoticeDTOs != null) {
            return new ResponseEntity<>(classNoticeDTOs, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * 수업 공지 페이지 별 + type 별 조회 컨트롤러 코드
     * @param classroom_id
     * @param page
     * @param types
     * @author DevTae
     */
    @GetMapping("/classroom/{classroom_id}/class-notice")
    public ResponseEntity<PageDTO<ClassNoticeDTO>> getClassNoticesByClassroom(@PathVariable("classroom_id") Long classroom_id,
                                                                              @RequestParam(name="page", defaultValue="0") Integer page,
                                                                              @RequestParam(name="type", required=false) List<ClassNoticeType> types) {
    	
    	PageDTO<ClassNoticeDTO> pageDTO = classNoticeService.getClassNoticesByClassroomId(classroom_id, page, types);
    	
    	if(pageDTO != null) {
    		return new ResponseEntity<>(pageDTO, HttpStatus.OK);
    	} else {
    		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    	}
    }
    
    
    /**
     * 특정 수업 공지를 조회하는 Controller입니다.
     * @param classroom_id classNotice_id
     * @return List<ClassNoticeDTO>
     * @author beom-i
     */
    @GetMapping("/classroom/{classroom_id}/class-notice/{classNotice_id}")
    public ResponseEntity<ClassNoticeDTO> getClassNoticeByClassroom(@PathVariable("classroom_id") Long classroom_id,@PathVariable("classNotice_id") Long classNotice_id) {

        ClassNoticeDTO classNoticeDTO = classNoticeService.getClassNoticeByClassroomIdAndClassNoticeId(classroom_id,classNotice_id);

        if (classNoticeDTO != null) {
            return new ResponseEntity<>(classNoticeDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
