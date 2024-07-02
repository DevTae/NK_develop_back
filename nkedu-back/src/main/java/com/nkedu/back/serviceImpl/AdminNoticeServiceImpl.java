package com.nkedu.back.serviceImpl;

import com.nkedu.back.api.AdminNoticeService;
import com.nkedu.back.dto.AdminDTO;
import com.nkedu.back.dto.AdminNoticeDTO;
import com.nkedu.back.dto.ClassNoticeDTO;
import com.nkedu.back.dto.PageDTO;
import com.nkedu.back.entity.Admin;
import com.nkedu.back.entity.AdminNotice;
import com.nkedu.back.entity.ClassNotice;
import com.nkedu.back.entity.AdminNotice.AdminNoticeType;
import com.nkedu.back.exception.errorCode.ClassErrorCode;
import com.nkedu.back.exception.exception.CustomException;
import com.nkedu.back.repository.AdminNoticeRepository;
import com.nkedu.back.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminNoticeServiceImpl implements AdminNoticeService {
    private final AdminNoticeRepository adminNoticeRepository;
    private final AdminRepository adminRepository;

    /**
     * ClassNotice 엔티티의 CRUD 관련 API 구현 코드입니다.
     * @author beom-i
     */

    @Override
    public boolean createAdminNotice(AdminNoticeDTO adminNoticeDTO) {
        try{
            Long admin_id = adminNoticeDTO.getAdminDTO().getId();
            Set<AdminNoticeType> adminNoticeType = adminNoticeDTO.getAdminNoticeType();

            Timestamp current_time = new Timestamp(System.currentTimeMillis());
            // Q. noticeDTO로 ID를 받지 않는데 어떻게 중복 검증을 할 수 있는가? A. 할 필요가 없어보임
//            if (!ObjectUtils.isEmpty(adminNoticeRepository.findOneById(adminNoticeDTO.getId()))) {
//                throw new RuntimeException("이미 등록된 공지입니다.");
//            }
            AdminNotice adminNotice = AdminNotice.builder()
                    .admin(adminRepository.findOneById(admin_id).get())
                    .title(adminNoticeDTO.getTitle())
                    .content(adminNoticeDTO.getContent())
                    .adminNoticeType(adminNoticeType)
                    .created(current_time)
                    .updated(current_time)
                    .build();

            adminNoticeRepository.save(adminNotice);
            return true;
        } catch(Exception e) {
            log.error("Failed: " + e.getMessage(),e);
        }
        return false;
    }

    @Override
    public boolean deleteAdminNoticeById(Long id) {
        try{
            adminNoticeRepository.deleteById(id);
            return true;
        } catch (Exception e){
            log.info("Failed: " + e.getMessage(),e);
        }
        return false;
    }

    @Override
    public boolean updateAdminNotice(Long id, AdminNoticeDTO adminNoticeDTO) {
        try{
            Optional<AdminNotice> optionalAdminNotice = adminNoticeRepository.findOneById(id);

            if (!optionalAdminNotice.isPresent()) {
                throw new CustomException(ClassErrorCode.ADMIN_NOTICE_NOT_FOUND);
            }

            AdminNotice searchedAdminNotice = optionalAdminNotice.get();

            if(!ObjectUtils.isEmpty(adminNoticeDTO.getTitle()))
                searchedAdminNotice.setTitle(adminNoticeDTO.getTitle());
            if(!ObjectUtils.isEmpty(adminNoticeDTO.getContent()))
                searchedAdminNotice.setContent(adminNoticeDTO.getContent());
            if(!ObjectUtils.isEmpty(adminNoticeDTO.getAdminNoticeType()))
                searchedAdminNotice.setAdminNoticeType(adminNoticeDTO.getAdminNoticeType());

            searchedAdminNotice.setUpdated(new Timestamp(System.currentTimeMillis()));

            adminNoticeRepository.save(searchedAdminNotice);
            return true;
        } catch(Exception e){
            throw e;
        }
    }

    @Override
    public List<AdminNoticeDTO> getAdminNotices() {
        try{
            List<AdminNoticeDTO> adminNoticeDTOs = new ArrayList<>();
            List<AdminNotice> adminNotices = null;

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            for (GrantedAuthority authority : authorities) {
                String authorityName = authority.getAuthority();

                // ROLE 에 따른 로직
                if (authorityName.equals("ROLE_ADMIN")) {
                    adminNotices = adminNoticeRepository.findAll();
                }
                else if (authorityName.equals("ROLE_STUDENT")) {
                    List<AdminNoticeType> types = Arrays.asList(AdminNoticeType.STUDENT);
                    adminNotices = adminNoticeRepository.findByAdminNoticeTypes(types).get();
                }
                else if (authorityName.equals("ROLE_TEACHER")) {
                    List<AdminNoticeType> types = Arrays.asList(AdminNoticeType.TEACHER);
                    adminNotices = adminNoticeRepository.findByAdminNoticeTypes(types).get();
                }
                else if (authorityName.equals("ROLE_PARENT")) {
                    List<AdminNoticeType> types = Arrays.asList(AdminNoticeType.PARENT);
                    adminNotices = adminNoticeRepository.findByAdminNoticeTypes(types).get();
                }
            }

            for(AdminNotice adminNotice : adminNotices){

//                Admin admin = adminRepository.findOneById(adminNotice.getAdmin().getId()).get();
//                if(ObjectUtils.isEmpty(admin)){
//                    throw new RuntimeException("공지를 작성한 admin이 존재하지 않음");
//                }

                AdminNoticeDTO adminNoticeDTO = AdminNoticeDTO.builder()
                        .id(adminNotice.getId())
                        .adminDTO(AdminDTO.builder().id(adminNotice.getAdmin().getId()).build())
                        .title(adminNotice.getTitle())
                        .content(adminNotice.getContent())
                        .created(adminNotice.getCreated())
                        .updated(adminNotice.getUpdated())
                        .adminNoticeType(adminNotice.getAdminNoticeType())
                        .build();
                adminNoticeDTOs.add(adminNoticeDTO);
            }
            return adminNoticeDTOs;
        }catch (Exception e){
            log.info("Failed : "+e.getMessage());
        }
        return null;
    }

    @Override
    public PageDTO<AdminNoticeDTO> getAdminNotices(Integer page, List<AdminNoticeType> types, String keyword) {
        try{
            
            PageDTO<AdminNoticeDTO> pageDTO = new PageDTO<>();
			List<AdminNoticeDTO> adminNoticeDTOs = new ArrayList<>();

			// 정렬 기준
			List<Sort.Order> sorts = new ArrayList<>();
			sorts.add(Sort.Order.desc("created"));
			Pageable pageable = PageRequest.of(page - 1, 8, Sort.by(sorts));

			// Page 조회
            Page<AdminNotice> pageOfAdminNotice = null;

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            for (GrantedAuthority authority : authorities) {
                String authorityName = authority.getAuthority();
                // ROLE 에 따른 로직
                if (authorityName.equals("ROLE_ADMIN")) {
                    // type값이 없을 경우 전체 type 조회
                    if(types == null || types.isEmpty()){
                        List<AdminNoticeType> type = Arrays.asList(AdminNoticeType.STUDENT,AdminNoticeType.TEACHER,AdminNoticeType.PARENT);
                        pageOfAdminNotice = adminNoticeRepository.findByAdminNoticeTypes(type,pageable,keyword);
                    }
                    // type값이 존재하면 해당하는 type에 따라 조회
                    else {
                        pageOfAdminNotice = adminNoticeRepository.findByAdminNoticeTypes(types,pageable,keyword);
                    }
                }
                else if (authorityName.equals("ROLE_STUDENT")) {
                    List<AdminNoticeType> type = Arrays.asList(AdminNoticeType.STUDENT);
                    pageOfAdminNotice = adminNoticeRepository.findByAdminNoticeTypes(type, pageable,keyword);
                }
                else if (authorityName.equals("ROLE_TEACHER")) {
                    List<AdminNoticeType> type = Arrays.asList(AdminNoticeType.TEACHER);
                    pageOfAdminNotice = adminNoticeRepository.findByAdminNoticeTypes(type, pageable,keyword);
                }
                else if (authorityName.equals("ROLE_PARENT")) {
                    List<AdminNoticeType> type = Arrays.asList(AdminNoticeType.PARENT);
                    pageOfAdminNotice = adminNoticeRepository.findByAdminNoticeTypes(type, pageable,keyword);
                }
            }
            
            // 페이지 정보 저장
            pageDTO.setCurrentPage(pageOfAdminNotice.getNumber() + 1);
			pageDTO.setTotalPage(pageOfAdminNotice.getTotalPages());

            for(AdminNotice adminNotice : pageOfAdminNotice.getContent()){

//                Admin admin = adminRepository.findOneById(adminNotice.getAdmin().getId()).get();
//                if(ObjectUtils.isEmpty(admin)){
//                    throw new RuntimeException("공지를 작성한 admin이 존재하지 않음");
//                }

                AdminNoticeDTO adminNoticeDTO = AdminNoticeDTO.builder()
                        .id(adminNotice.getId())
                        .adminDTO(AdminDTO.builder().id(adminNotice.getAdmin().getId()).build())
                        .title(adminNotice.getTitle())
                        .content(adminNotice.getContent())
                        .created(adminNotice.getCreated())
                        .updated(adminNotice.getUpdated())
                        .adminNoticeType(adminNotice.getAdminNoticeType())
                        .build();
                adminNoticeDTOs.add(adminNoticeDTO);
            }
            
            // 페이지 검색 결과 저장
            pageDTO.setResults(adminNoticeDTOs);;
            
            return pageDTO;
            
        }catch (Exception e){
            log.info("Failed : "+e.getMessage());
        }
        return null;
    }



    @Override
    public AdminNoticeDTO getAdminNotice(Long adminNotice_id) {
        try{
            AdminNotice adminNotice = adminNoticeRepository.findOneById(adminNotice_id).get();

            AdminNoticeDTO adminNoticeDTO = AdminNoticeDTO.builder()
                    .id(adminNotice.getId())
                    .adminDTO(AdminDTO.builder().id(adminNotice.getAdmin().getId()).build())
                    .title(adminNotice.getTitle())
                    .content(adminNotice.getContent())
                    .created(adminNotice.getCreated())
                    .updated(adminNotice.getUpdated())
                    .adminNoticeType(adminNotice.getAdminNoticeType())
                    .build();
            return adminNoticeDTO;
        } catch(Exception e){
            log.info("Failed : " + e.getMessage());
        }
        return null;
    }
}
