package com.nkedu.back.controller;

import com.nkedu.back.api.ClassNoticeService;
import com.nkedu.back.dto.ClassNoticeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ClassNoticeController {
    private final ClassNoticeService classNoticeService;


    /**
     * 수업 공지를 생성하는 controller 입니다.
     * @param classNoticeDTO
     *
     * @author beom-i
     */
    @PostMapping("/classroom/{classroom_id}/class-notice")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER')")
    public ResponseEntity<Void> createClassNotice(@PathVariable("classroom_id") Long classroom_id,@Validated @RequestBody ClassNoticeDTO classNoticeDTO) {

        boolean result = classNoticeService.createClassNotice(classroom_id,classNoticeDTO);

        if (result == true) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 특정 id의 수업 공지를 수정하는 controller 입니다.
     * (제목, 내용, 공지 공개 범위만 수정할 수 있습니다.)
     * @param classroom_id,notice_id, classNoticeDTO
     * @author beom-i
     */
    @PutMapping("/classroom/{classroom_id}/class-notice/{notice_id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER')")
    public ResponseEntity<Void> updateClassNotice(@PathVariable("classroom_id") Long classroom_id,@PathVariable("notice_id") Long notice_id, @RequestBody ClassNoticeDTO classNoticeDTO) {

        boolean result = classNoticeService.updateClassNotice(classroom_id,notice_id, classNoticeDTO);

        if (result == true) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 특정 수업 공지를 삭제하는 controller 입니다.
     * @param classroom_id, notice_id
     * @author beom-i
     */
    @DeleteMapping("/classroom/{classroom_id}/class-notice/{notice_id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER')")
    public ResponseEntity<Void> deleteClassNotice(@PathVariable("classroom_id") Long classroom_id,@PathVariable("notice_id") Long notice_id) {

        boolean result = classNoticeService.deleteClassNoticeById(classroom_id,notice_id);

        if (result == true) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
