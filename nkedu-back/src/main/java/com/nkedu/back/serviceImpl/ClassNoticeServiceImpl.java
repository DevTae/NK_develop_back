package com.nkedu.back.serviceImpl;

import com.nkedu.back.api.ClassNoticeService;
import com.nkedu.back.dto.ClassroomDTO;
import com.nkedu.back.dto.PageDTO;
import com.nkedu.back.dto.ClassNoticeDTO;
import com.nkedu.back.dto.TeacherDTO;
import com.nkedu.back.entity.ClassNotice;
import com.nkedu.back.entity.Classroom;
import com.nkedu.back.entity.ClassNotice.ClassNoticeType;
import com.nkedu.back.entity.Teacher;
import com.nkedu.back.repository.ClassroomRepository;
import com.nkedu.back.repository.ClassNoticeRepository;
import com.nkedu.back.repository.TeacherRepository;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassNoticeServiceImpl implements ClassNoticeService {
    private final ClassNoticeRepository classNoticeRepository;
    private final TeacherRepository teacherRepository;
    private final ClassroomRepository classroomRepository;

    /**
     * ClassNotice 엔티티의 CRUD 관련 API 구현 코드입니다.
     * @author beom-i
     */

    @Override
    public boolean createClassNotice(Long classroom_id, ClassNoticeDTO classNoticeDTO) {

        try{
            Long teacher_id = classNoticeDTO.getTeacherDTO().getId();
            ClassNoticeType classNoticeType = classNoticeDTO.getClassNoticeType();

            // classNotice DTO 내 teacher가 classroom_id에 담당하고 있는 선생님인지 검증이 필요
            // 관리자는 어떻게 대응할지?

            Timestamp current_time = new Timestamp(System.currentTimeMillis());

            ClassNotice classNotice = ClassNotice.builder()
                    .teacher(teacherRepository.findOneById(teacher_id).get())
                    .classroom(classroomRepository.findOneClassroomById(classroom_id).get())
                    .title(classNoticeDTO.getTitle())
                    .content(classNoticeDTO.getContent())
                    .created(current_time)
                    .updated(current_time)
                    .classNoticeType(classNoticeType)
                    .build();

            classNoticeRepository.save(classNotice);
            return true;
        } catch(Exception e) {
            log.error("Failed: " + e.getMessage(),e);
        }
        return false;
    }

    @Override
    public boolean deleteClassNoticeById(Long classroom_id, Long notice_id) {
        try{
            // 수업과 공지에 동시에 해당하는 공지를 삭제
            classNoticeRepository.deleteByClassroomIdAndClassNoticeId(classroom_id,notice_id);
            return true;
        }catch (Exception e){
            log.info("Failed: " + e.getMessage(),e);
        }
        return false;
    }

    @Override
    public boolean updateClassNotice(Long classroom_id, Long notice_id, ClassNoticeDTO classNoticeDTO) {

        try {
            // classroom_id, notice_id 가 동시에 같은 공지를 가져옴
            ClassNotice searchedClassNotice = classNoticeRepository.findOneByClassroomIdAndClassNoticeId(classroom_id,notice_id).get();

            // 찾는 수업 공지가 없다면 중단
            if(ObjectUtils.isEmpty(searchedClassNotice))
                return false;

            if(!ObjectUtils.isEmpty(classNoticeDTO.getTitle()))
                searchedClassNotice.setTitle(classNoticeDTO.getTitle());
            if(!ObjectUtils.isEmpty(classNoticeDTO.getContent()))
                searchedClassNotice.setContent(classNoticeDTO.getContent());
            if(!ObjectUtils.isEmpty(classNoticeDTO.getClassNoticeType()))
                searchedClassNotice.setClassNoticeType(classNoticeDTO.getClassNoticeType());

            searchedClassNotice.setUpdated(new Timestamp(System.currentTimeMillis()));

            classNoticeRepository.save(searchedClassNotice);

            return true;
        } catch (Exception e) {
            log.info("[Failed] e : " + e.getMessage());
        }
        return false;
    }


    @Override
    public List<ClassNoticeDTO> getClassNoticesByClassroomId(Long id) {
        try {
            List<ClassNoticeDTO> classNoticeDTOs = new ArrayList<>();

            List<ClassNotice> classNotices = null;

            // 토큰에서 ROLE을 가져와서 ROLE에 따라 공지 타입 필터링 조회
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            for (GrantedAuthority authority : authorities) {
                String authorityName = authority.getAuthority();

                if (authorityName.equals("ROLE_ADMIN") || authorityName.equals("ROLE_TEACHER")) {
                    classNotices = classNoticeRepository.findAllByClassroomId(id).get();
                }
                else if (authorityName.equals("ROLE_STUDENT")) {
                    List<ClassNoticeType> types = Arrays.asList(ClassNoticeType.STUDENT, ClassNoticeType.ENTIRE);
                    classNotices = classNoticeRepository.findByClassroomIdAndClassNoticeTypes(id,types).get();
                }
                else if (authorityName.equals("ROLE_PARENT")) {
                    List<ClassNoticeType> types = Arrays.asList(ClassNoticeType.PARENT, ClassNoticeType.ENTIRE);
                    classNotices = classNoticeRepository.findByClassroomIdAndClassNoticeTypes(id,types).get();
                }
            }

            for(ClassNotice classNotice : classNotices) {
                Teacher teacher = teacherRepository.findOneById(classNotice.getTeacher().getId()).get();

                if(ObjectUtils.isEmpty(teacher)) {
                    throw new RuntimeException("공지를 작성한 사용자가 존재하지 않습니다.");
                }

                ClassNoticeDTO classNoticeDTO = ClassNoticeDTO.builder()
                        .id(classNotice.getId())
                        .classroomDTO(ClassroomDTO.builder().id(classNotice.getClassroom().getId()).build())
                        .teacherDTO(TeacherDTO.builder().id(teacher.getId()).build())
                        .title(classNotice.getTitle())
                        .content(classNotice.getContent())
                        .created(classNotice.getCreated())
                        .updated(classNotice.getUpdated())
                        .classNoticeType(classNotice.getClassNoticeType())
                        .build();
                classNoticeDTOs.add(classNoticeDTO);
            }
            return classNoticeDTOs;
        } catch(Exception e) {
            log.info("[Failed] e : " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public PageDTO<ClassNoticeDTO> getClassNoticesByClassroomId(Long id, Integer page) {
        try {
        	
        	PageDTO<ClassNoticeDTO> pageDTO = new PageDTO<>();
			List<ClassNoticeDTO> classNoticeDTOs = new ArrayList<>();

			// 정렬 기준
			List<Sort.Order> sorts = new ArrayList<>();
			sorts.add(Sort.Order.desc("created"));
			Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

			// Page 조회
            Page<ClassNotice> pageOfClassNotice = null;

            // 토큰에서 ROLE을 가져와서 ROLE에 따라 공지 타입 필터링 조회
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            for (GrantedAuthority authority : authorities) {
                String authorityName = authority.getAuthority();

                if (authorityName.equals("ROLE_ADMIN") || authorityName.equals("ROLE_TEACHER")) {
                	pageOfClassNotice = classNoticeRepository.findAllByClassroomId(id, pageable);
                }
                else if (authorityName.equals("ROLE_STUDENT")) {
                    List<ClassNoticeType> types = Arrays.asList(ClassNoticeType.STUDENT, ClassNoticeType.ENTIRE);
                    pageOfClassNotice = classNoticeRepository.findByClassroomIdAndClassNoticeTypes(id, types, pageable);
                }
                else if (authorityName.equals("ROLE_PARENT")) {
                    List<ClassNoticeType> types = Arrays.asList(ClassNoticeType.PARENT, ClassNoticeType.ENTIRE);
                    pageOfClassNotice = classNoticeRepository.findByClassroomIdAndClassNoticeTypes(id, types, pageable);
                }
            }
            
            // 페이지 정보 추가
            pageDTO.setCurrentPage(pageOfClassNotice.getNumber());
			pageDTO.setTotalPage(pageOfClassNotice.getTotalPages());

            for(ClassNotice classNotice : pageOfClassNotice.getContent()) {
                Teacher teacher = teacherRepository.findOneById(classNotice.getTeacher().getId()).get();

                if(ObjectUtils.isEmpty(teacher)) {
                    throw new RuntimeException("공지를 작성한 사용자가 존재하지 않습니다.");
                }

                ClassNoticeDTO classNoticeDTO = ClassNoticeDTO.builder()
                        .id(classNotice.getId())
                        .classroomDTO(ClassroomDTO.builder().id(classNotice.getClassroom().getId()).build())
                        .teacherDTO(TeacherDTO.builder().id(teacher.getId()).build())
                        .title(classNotice.getTitle())
                        .content(classNotice.getContent())
                        .created(classNotice.getCreated())
                        .updated(classNotice.getUpdated())
                        .classNoticeType(classNotice.getClassNoticeType())
                        .build();
                classNoticeDTOs.add(classNoticeDTO);
            }
            
            // 페이지 검색 결과 추가
            pageDTO.setResults(classNoticeDTOs);
            
            return pageDTO;
            
        } catch(Exception e) {
            log.info("[Failed] e : " + e.getMessage());
        }
        return null;
    }

    @Override
    public ClassNoticeDTO getClassNoticeByClassroomIdAndClassNoticeId(Long classroom_id,Long classNotice_id) {
        try{
            ClassNotice classNotice = classNoticeRepository.findOneByClassroomIdAndClassNoticeId(classroom_id,classNotice_id).get();
            Teacher teacher = teacherRepository.findOneById(classNotice.getTeacher().getId()).get();


            ClassNoticeDTO classNoticeDTO = ClassNoticeDTO.builder()
                    .id(classNotice.getId())
                    .classroomDTO(ClassroomDTO.builder().id(classNotice.getClassroom().getId()).build())
                    .teacherDTO(TeacherDTO.builder().id(teacher.getId()).build())
                    .title(classNotice.getTitle())
                    .content(classNotice.getContent())
                    .created(classNotice.getCreated())
                    .updated(classNotice.getUpdated())
                    .classNoticeType(classNotice.getClassNoticeType())
                    .build();

            return classNoticeDTO;
        } catch(Exception e){
            log.info("Failed e : " + e.getMessage());
        }
        return null;
    }

}
