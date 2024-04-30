package com.nkedu.back.controller;

import com.nkedu.back.api.AdminNoticeService;
import com.nkedu.back.dto.AdminNoticeDTO;
import com.nkedu.back.dto.PageDTO;
import com.nkedu.back.entity.AdminNotice.AdminNoticeType;
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
public class AdminNoticeController {
    private final AdminNoticeService adminNoticeService;

    /**
     * 관리자 공지를 생성하는 controller 입니다.
     * @param adminNoticeDTO
     *
     * @author beom-i
     */
    @PostMapping("/admin-notice")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> createAdminNotice(@Validated @RequestBody AdminNoticeDTO adminNoticeDTO) {

        boolean result = adminNoticeService.createAdminNotice(adminNoticeDTO);

        if (result == true) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 특정 id의 관리자 공지를 수정하는 controller 입니다.
     * (제목, 내용, 공지 공개 범위만 수정할 수 있습니다.)
     * @param id, classNoticeDTO
     * @author beom-i
     */
    @PutMapping("/admin-notice/{id}")
    public ResponseEntity<Void> updateAdminNotice(@PathVariable("id") Long id, @RequestBody AdminNoticeDTO adminNoticeDTO) {

        boolean result = adminNoticeService.updateAdminNotice(id, adminNoticeDTO);

        if (result == true) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 특정 관리자 공지를 삭제하는 controller 입니다.
     * @param id
     *
     * @author beom-i
     */

    @DeleteMapping("/admin-notice/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteAdminNotice(@PathVariable("id") Long id){
        boolean result = adminNoticeService.deleteAdminNoticeById(id);

        if (result == true) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 전체 관리자 공지를 조회하는 Controller입니다.
     * @return List<AdminNoticeDTO>
     * @author beom-i
     */
    @GetMapping("/admin-notice/list")
    public ResponseEntity<List<AdminNoticeDTO>> getAdminNotices(){

        List<AdminNoticeDTO> adminNoticeDTOs = adminNoticeService.getAdminNotices();

        if (adminNoticeDTOs != null) {
            return new ResponseEntity<>(adminNoticeDTOs, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 관리자 공지 페이지 별 + type 별 조회 컨트롤러
     * @param page
     * @param types
     * @author DevTae
     */
    @GetMapping("/admin-notice")
    public ResponseEntity<PageDTO<AdminNoticeDTO>> getAdminNotices(@RequestParam(name="page", defaultValue="0") Integer page,
                                                                   @RequestParam(name="type", required=false) List<AdminNoticeType> types) {


    	PageDTO<AdminNoticeDTO> pageDTO = adminNoticeService.getAdminNotices(page,types);
    	
    	if (pageDTO != null) {
    		return new ResponseEntity<>(pageDTO, HttpStatus.OK);
    	} else {
    		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    	}
    }

    /**
     * 특정 관리자 공지를 조회하는 Controller입니다.
     * @param id
     * @return AdminNoticeDTO
     * @author beom-i
     */
    @GetMapping("/admin-notice/{id}")
    public ResponseEntity<AdminNoticeDTO> getAdminNotice(@PathVariable("id") Long id){
        AdminNoticeDTO adminNoticeDTO = adminNoticeService.getAdminNotice(id);

        if (adminNoticeDTO != null) {
            return new ResponseEntity<>(adminNoticeDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


}
