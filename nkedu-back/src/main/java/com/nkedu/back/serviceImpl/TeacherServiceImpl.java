package com.nkedu.back.serviceImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nkedu.back.dto.ClassroomDTO;
import com.nkedu.back.dto.PageDTO;
import com.nkedu.back.dto.ParentDTO;
import com.nkedu.back.dto.StudentDTO;
import com.nkedu.back.dto.TeacherOfClassroomDTO;
import com.nkedu.back.dto.TeacherWithClassroomDTO;
import com.nkedu.back.entity.Authority;
import com.nkedu.back.entity.Parent;
import com.nkedu.back.entity.ParentOfStudent;
import com.nkedu.back.entity.Student;
import com.nkedu.back.entity.TeacherOfClassroom;
import com.nkedu.back.repository.TeacherOfClassroomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.nkedu.back.api.TeacherService;
import com.nkedu.back.entity.Teacher;
import com.nkedu.back.dto.TeacherDTO;
import com.nkedu.back.repository.TeacherRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherOfClassroomRepository teacherOfclassroomRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public boolean createTeacher(TeacherDTO teacherDTO) {
        try {
            if (!ObjectUtils.isEmpty(teacherRepository.findOneByUsername(teacherDTO.getUsername()))) {
                throw new RuntimeException("이미 가입되어 있는 유저입니다.");
            }

            System.out.println("getUserName: " + teacherDTO.getUsername());

            Set<Authority> authorities = new HashSet<Authority>();

            Authority authority_user = Authority.builder()
                    .authorityName("ROLE_USER")
                    .build();
            authorities.add(authority_user);

            Authority authority_teacher = Authority.builder()
                    .authorityName("ROLE_TEACHER")
                    .build();
            authorities.add(authority_teacher);

            Teacher teacher = (Teacher) Teacher.builder()
                    .username(teacherDTO.getUsername())
                    .password(passwordEncoder.encode(teacherDTO.getPassword()))
                    .nickname(teacherDTO.getNickname())
                    .birth(teacherDTO.getBirth())
                    .phoneNumber(teacherDTO.getPhoneNumber())
                    .authorities(authorities)
                    .created(new Timestamp(System.currentTimeMillis()))
                    .activated(true)
                    .registrationDate(teacherDTO.getRegistrationDate())
                    .workingDays(teacherDTO.getWorkingDays())
                    .build();

            teacherRepository.save(teacher);
            return true;
        } catch (Exception e) {
            log.error("Failed: " + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean deleteByUsername(String username) {
        try {
        	Teacher teacher = teacherRepository.findOneByUsername(username).get();
        	teacher.setActivated(false);;
            teacherRepository.save(teacher);

            return true;
        } catch (Exception e) {
            log.info("[Failed] e : " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateTeacher(String username, TeacherDTO teacherDTO) {
        try {
            Teacher searchedTeacher = teacherRepository.findOneByUsername(username).get();

            if (ObjectUtils.isEmpty(searchedTeacher))
                return false;

            if (!ObjectUtils.isEmpty(teacherDTO.getUsername()))
                searchedTeacher.setUsername(teacherDTO.getUsername());
            if (!ObjectUtils.isEmpty(teacherDTO.getPassword()))
                searchedTeacher.setPassword(teacherDTO.getPassword());
            if (!ObjectUtils.isEmpty(teacherDTO.getNickname()))
                searchedTeacher.setNickname(teacherDTO.getNickname());
            if (!ObjectUtils.isEmpty(teacherDTO.getPhoneNumber()))
                searchedTeacher.setPhoneNumber(teacherDTO.getPhoneNumber());
            if (!ObjectUtils.isEmpty(teacherDTO.getBirth()))
                searchedTeacher.setBirth(teacherDTO.getBirth());
            if (!ObjectUtils.isEmpty(teacherDTO.getRegistrationDate()))
                searchedTeacher.setRegistrationDate(teacherDTO.getRegistrationDate());
            if (!ObjectUtils.isEmpty(teacherDTO.getWorkingDays()))
                searchedTeacher.setWorkingDays(teacherDTO.getWorkingDays());

            teacherRepository.save(searchedTeacher);

            return true;
        } catch (Exception e) {
            log.info("[Failed] e : " + e.getMessage());
        }

        return false;
    }

    @Override
    public List<TeacherDTO> getTeachers() {
        try {
            List<TeacherDTO> teacherDTOs = new ArrayList<>();

            List<Teacher> teachers = teacherRepository.findAll();

            for (Teacher teacher : teachers) {
                TeacherDTO teacherDTO = new TeacherDTO();
                teacherDTO.setId(teacher.getId());
                teacherDTO.setUsername(teacher.getUsername());
                teacherDTO.setNickname(teacher.getNickname());
                teacherDTO.setPhoneNumber(teacher.getPhoneNumber());
                teacherDTO.setBirth(teacher.getBirth());
                teacherDTO.setRegistrationDate(teacher.getRegistrationDate());
                teacherDTO.setWorkingDays(teacher.getWorkingDays());

                teacherDTOs.add(teacherDTO);
            }

            return teacherDTOs;
        } catch (Exception e) {
            log.info("[Failed] e : " + e.getMessage());
        }

        return null;
    }
    
    @Override
    public PageDTO<TeacherDTO> getTeachersByKeyword(Integer page,String keyword) {
    	try {
			PageDTO<TeacherDTO> pageDTO = new PageDTO<>();
			List<TeacherDTO> teacherDTOs = new ArrayList<>();

			// 정렬 기준
			List<Sort.Order> sorts = new ArrayList<>();
			sorts.add(Sort.Order.asc("nickname"));
			Pageable pageable = PageRequest.of(page, 16, Sort.by(sorts));

			// Page 조회
            Page<Teacher> pageOfTeacher = teacherRepository.findAllByTeacherName(pageable,keyword);
			
			pageDTO.setCurrentPage(pageOfTeacher.getNumber());
			pageDTO.setTotalPage(pageOfTeacher.getTotalPages());
			
			for(Teacher teacher : pageOfTeacher.getContent()) {
				TeacherDTO teacherDTO = new TeacherDTO();
                teacherDTO.setId(teacher.getId());
                teacherDTO.setUsername(teacher.getUsername());
                teacherDTO.setNickname(teacher.getNickname());
                teacherDTO.setPhoneNumber(teacher.getPhoneNumber());
                teacherDTO.setBirth(teacher.getBirth());
                teacherDTO.setRegistrationDate(teacher.getRegistrationDate());
                teacherDTO.setWorkingDays(teacher.getWorkingDays());

                teacherDTOs.add(teacherDTO);
			}
			
			pageDTO.setResults(teacherDTOs);
			
			return pageDTO;
			
		} catch(Exception e) {
			log.info("[Failed] e : " + e.getMessage());
		}
		return null;
    }
    @Override
    public TeacherDTO getTeacherByUsername(String username) {

        try {
            Teacher teacher = teacherRepository.findOneByUsername(username).get();

            TeacherDTO teacherDTO = new TeacherDTO();
            teacherDTO.setId(teacher.getId());
            teacherDTO.setUsername(teacher.getUsername());
            teacherDTO.setNickname(teacher.getNickname());
            teacherDTO.setPhoneNumber(teacher.getPhoneNumber());
            teacherDTO.setBirth(teacher.getBirth());
            teacherDTO.setRegistrationDate(teacher.getRegistrationDate());
            teacherDTO.setWorkingDays(teacher.getWorkingDays());

            return teacherDTO;

        } catch (Exception e) {
            log.info("[Failed] e : " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<TeacherWithClassroomDTO> getTeachersWithClassroom() {
        try {
            // 조회할 모든 선생님을 담을 리스트
            List<TeacherWithClassroomDTO> TeacherWithClassroomDTOs = new ArrayList<>();

            List<Teacher> teachers = teacherRepository.findAll();

            for (Teacher teacher : teachers) {

                TeacherWithClassroomDTO teacherWithClassroomDTO = new TeacherWithClassroomDTO();

                teacherWithClassroomDTO.setId(teacher.getId());
                teacherWithClassroomDTO.setUsername(teacher.getUsername());
                teacherWithClassroomDTO.setNickname(teacher.getNickname());
                teacherWithClassroomDTO.setPhoneNumber(teacher.getPhoneNumber());
                teacherWithClassroomDTO.setBirth(teacher.getBirth());
                teacherWithClassroomDTO.setRegistrationDate(teacher.getRegistrationDate());
                teacherWithClassroomDTO.setWorkingDays(teacher.getWorkingDays());

                // 1. 선생님이 담당하고 있는 반의 리스트를 생성
                List<TeacherOfClassroomDTO> teacherOfClassroomDTOs = new ArrayList<>();
                // 2. 선생님이 담당하고 있는 선생-반 모든 관계테이블을 가져옴
                List<TeacherOfClassroom> teacherOfClassrooms = teacherOfclassroomRepository.findAllByTeacherId(teacher.getId());
                // 3. 모든 관계테이블을 돌면서 선생님이 담당하고 있는 반의 리스트에 추가시킴
                for(TeacherOfClassroom teacherOfClassroom : teacherOfClassrooms){
                    TeacherOfClassroomDTO TC = TeacherOfClassroomDTO.builder()
                            .classroomDTO(ClassroomDTO.builder()
                                    .id(teacherOfClassroom.getClassroom().getId())
                                    .classname(teacherOfClassroom.getClassroom().getClassname())
                                    .build())
                            .type(teacherOfClassroom.isType()).build();
                    teacherOfClassroomDTOs.add(TC);
                }
                // 4. 선생님의 DTO에 선생님이 담당하고 있는 반의 리스트를 넣음.
                teacherWithClassroomDTO.setTeacherOfClassroomDTO(teacherOfClassroomDTOs);

                // 5. 모든 선생님 리스트에 추가함.
                TeacherWithClassroomDTOs.add(teacherWithClassroomDTO);
            }

            return TeacherWithClassroomDTOs;
        } catch (Exception e) {
            log.info("[Failed] e : " + e.getMessage());
        }
        return null;
    }

    @Override
    public TeacherWithClassroomDTO getTeacherWithClassroom(String username) {
        try {

            Teacher teacher = teacherRepository.findOneByUsername(username).get();

            TeacherWithClassroomDTO teacherWithClassroomDTO = new TeacherWithClassroomDTO();

            teacherWithClassroomDTO.setId(teacher.getId());
            teacherWithClassroomDTO.setUsername(teacher.getUsername());
            teacherWithClassroomDTO.setNickname(teacher.getNickname());
            teacherWithClassroomDTO.setPhoneNumber(teacher.getPhoneNumber());
            teacherWithClassroomDTO.setBirth(teacher.getBirth());
            teacherWithClassroomDTO.setRegistrationDate(teacher.getRegistrationDate());
            teacherWithClassroomDTO.setWorkingDays(teacher.getWorkingDays());

            // 1. 선생님이 담당하고 있는 반의 리스트를 생성
            List<TeacherOfClassroomDTO> teacherOfClassroomDTOs = new ArrayList<>();
            // 2. 선생님이 담당하고 있는 선생-반 모든 관계테이블을 가져옴
            List<TeacherOfClassroom> teacherOfClassrooms = teacherOfclassroomRepository.findAllByTeacherId(teacher.getId());
            // 3. 모든 관계테이블을 돌면서 선생님이 담당하고 있는 반의 리스트에 추가시킴
            for(TeacherOfClassroom teacherOfClassroom : teacherOfClassrooms){
                TeacherOfClassroomDTO TC = TeacherOfClassroomDTO.builder()
                        .classroomDTO(ClassroomDTO.builder()
                                .id(teacherOfClassroom.getClassroom().getId())
                                .classname(teacherOfClassroom.getClassroom().getClassname())
                                .build())
                        .type(teacherOfClassroom.isType()).build();
                teacherOfClassroomDTOs.add(TC);
            }
            // 4. 선생님의 DTO에 선생님이 담당하고 있는 반의 리스트를 넣음.
            teacherWithClassroomDTO.setTeacherOfClassroomDTO(teacherOfClassroomDTOs);

            return teacherWithClassroomDTO;
        } catch (Exception e) {
            log.info("[Failed] e : " + e.getMessage());
        }
        return null;
    }
}