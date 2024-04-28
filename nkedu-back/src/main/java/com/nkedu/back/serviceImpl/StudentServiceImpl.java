package com.nkedu.back.serviceImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.nkedu.back.entity.Authority;
import com.nkedu.back.entity.Parent;
import com.nkedu.back.entity.ParentOfStudent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.nkedu.back.api.StudentService;
import com.nkedu.back.entity.School;
import com.nkedu.back.entity.Student;
import com.nkedu.back.dto.PageDTO;
import com.nkedu.back.dto.ParentDTO;
import com.nkedu.back.dto.StudentDTO;
import com.nkedu.back.repository.ParentOfStudentRepository;
import com.nkedu.back.repository.SchoolRepository;
import com.nkedu.back.repository.StudentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService  {

	private final StudentRepository studentRepository;
	private final SchoolRepository schoolRepository;
	private final ParentOfStudentRepository parentOfStudentRepository;
	private final PasswordEncoder passwordEncoder;

	// 학생 계정 생성
	public boolean createStudent(StudentDTO studentDTO) {
		try {

			if (!ObjectUtils.isEmpty(studentRepository.findOneByUsername(studentDTO.getUsername()))) {
				throw new RuntimeException("이미 가입되어 있는 유저입니다.");
			}

			//1. 요청온 학교가 기존 학교 DB에 존재하지 않으면 오류 발생
			School postedSchool = studentDTO.getSchool();
			if(ObjectUtils.isEmpty(schoolRepository.findOneBySchoolName(postedSchool.getSchoolName()))){
				throw new RuntimeException("존재하지 않는 학교 이름입니다.");
			}

			Set<Authority> authorities = new HashSet<>();

			Authority authority_user = Authority.builder()
					.authorityName("ROLE_USER")
					.build();
			authorities.add(authority_user);

			Authority authority_student = Authority.builder()
					.authorityName("ROLE_STUDENT")
					.build();
			authorities.add(authority_student);

			Student student = (Student) Student.builder()
					.username(studentDTO.getUsername())
					.password(passwordEncoder.encode(studentDTO.getPassword()))
					.nickname(studentDTO.getNickname())
					.birth(studentDTO.getBirth())
					.phoneNumber(studentDTO.getPhoneNumber())
					.authorities(authorities)
					.created(new Timestamp(System.currentTimeMillis()))
					.activated(true)
					.grade(studentDTO.getGrade()) // grade 추가
					.school(studentDTO.getSchool()) // school 추가
					.build();

			studentRepository.save(student);
			return true;
		} catch (Exception e) {
			log.error("Failed: " + e.getMessage(), e);
		}
		return false;
	}

	// 학생 계정 삭제
	public boolean deleteByUsername(String username) {
		try{
			Student student = studentRepository.findOneByUsername(username).get();
			student.setActivated(false);
			studentRepository.save(student);

			return true;
		} catch (Exception e){
			log.info("failed e : " + e.getMessage());
		}
		return false;
	}

	// 학생 계정 수정
	// 학교 수정 API는 따로 만들어야 할 듯 (ex. 관리자)
	// grade 도 매년 자동으로 올릴지.. 어떻게 해야할지 고민
	public boolean updateStudent(String username, StudentDTO studentDTO) {
		try {
			Student searchedStudent = studentRepository.findOneByUsername(username).get();

			if(ObjectUtils.isEmpty(searchedStudent))
				return false;

			// 요청 받은 학생 이름으로 업데이트
			if(!ObjectUtils.isEmpty(studentDTO.getUsername()))
				searchedStudent.setUsername(studentDTO.getUsername());
			if(!ObjectUtils.isEmpty(studentDTO.getPassword()))
				searchedStudent.setPassword(studentDTO.getPassword());
			if(!ObjectUtils.isEmpty(studentDTO.getNickname()))
				searchedStudent.setNickname(studentDTO.getNickname());
			if(!ObjectUtils.isEmpty(studentDTO.getPhoneNumber()))
				searchedStudent.setPhoneNumber(studentDTO.getPhoneNumber());
			if(!ObjectUtils.isEmpty(studentDTO.getBirth()))
				searchedStudent.setBirth(studentDTO.getBirth());
			if(!ObjectUtils.isEmpty(studentDTO.getGrade()))
				searchedStudent.setGrade(studentDTO.getGrade());
			if(!ObjectUtils.isEmpty(studentDTO.getSchool()))
				searchedStudent.setSchool(studentDTO.getSchool());

			studentRepository.save(searchedStudent);
			return true;
		} catch (Exception e) {
			log.info("[Failed] e : " + e.getMessage());
		}
		return false;
	}

	// 모든 학생  계정 리스트 조회
	public List<StudentDTO> getStudents() {
		try {
			// 업데이트된 studentDTO 담을 배
			List<StudentDTO> studentDTOs = new ArrayList<>();

			List<Student> students = studentRepository.findAll();

			for(Student student : students) {
				StudentDTO studentDTO = new StudentDTO();
				studentDTO.setId(student.getId());
				studentDTO.setUsername(student.getUsername());
				studentDTO.setNickname(student.getNickname());
				studentDTO.setBirth(student.getBirth());
				studentDTO.setPhoneNumber(student.getPhoneNumber());

				studentDTO.setSchool(student.getSchool());
				studentDTO.setGrade(student.getGrade());
				
				// 학생에 대한 부모님 정보 추가
				List<ParentDTO> parentDTOs = new ArrayList<ParentDTO>();
				List<ParentOfStudent> parentOfStudents = parentOfStudentRepository.findAllByStudentname(student.getUsername()).get();
				for(int i = 0; i < parentOfStudents.size(); i++) {
					Parent parent = parentOfStudents.get(i).getParent();
					
					ParentDTO parentDTO = ParentDTO.builder()
												   .id(parent.getId())
												   .nickname(parent.getNickname())
												   .relationship(parentOfStudents.get(i).getRelationship())
												   .build();
					
					parentDTOs.add(parentDTO);
				}
				studentDTO.setParentDTOs(parentDTOs);

				studentDTOs.add(studentDTO);
			}
			return studentDTOs;
		} catch(Exception e) {
			log.info("[Failed] e : " + e.getMessage());
		}
		return null;
	}
	
	public PageDTO<StudentDTO> getStudents(Integer page) {
		try {
			PageDTO<StudentDTO> pageDTO = new PageDTO<>();
			List<StudentDTO> studentDTOs = new ArrayList<>();

			// 정렬 기준
			List<Sort.Order> sorts = new ArrayList<>();
			sorts.add(Sort.Order.desc("created"));
			Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

			// Page 조회
			Page<Student> pageOfStudent = studentRepository.findAll(pageable);
			
			pageDTO.setCurrentPage(pageOfStudent.getNumber());
			pageDTO.setTotalPage(pageOfStudent.getTotalPages());
			
			for(Student student : pageOfStudent.getContent()) {
				StudentDTO studentDTO = new StudentDTO();
				studentDTO.setId(student.getId());
				studentDTO.setUsername(student.getUsername());
				studentDTO.setNickname(student.getNickname());
				studentDTO.setBirth(student.getBirth());
				studentDTO.setPhoneNumber(student.getPhoneNumber());

				studentDTO.setSchool(student.getSchool());
				studentDTO.setGrade(student.getGrade());
				
				// 학생에 대한 부모님 정보 추가
				List<ParentDTO> parentDTOs = new ArrayList<ParentDTO>();
				List<ParentOfStudent> parentOfStudents = parentOfStudentRepository.findAllByStudentname(student.getUsername()).get();
				for(int i = 0; i < parentOfStudents.size(); i++) {
					Parent parent = parentOfStudents.get(i).getParent();
					
					ParentDTO parentDTO = ParentDTO.builder()
												   .id(parent.getId())
												   .nickname(parent.getNickname())
												   .relationship(parentOfStudents.get(i).getRelationship())
												   .build();
					
					parentDTOs.add(parentDTO);
				}
				studentDTO.setParentDTOs(parentDTOs);

				studentDTOs.add(studentDTO);
			}
			
			pageDTO.setResults(studentDTOs);
			
			return pageDTO;
			
		} catch(Exception e) {
			log.info("[Failed] e : " + e.getMessage());
		}
		return null;
	}


	// 특정 학생 계정 정보 조회
	// 어떤 정보만을 넘길지는 추후 피드백을 통해 API 추가로 만들면 됨
	public StudentDTO findByUsername(String username) {
		try {
			Student student = studentRepository.findOneByUsername(username).get();

			// 특정 학생의 계정 정보를 담을 DTO 생성
			StudentDTO studentDTO = new StudentDTO();

			studentDTO.setId(student.getId());
			studentDTO.setUsername(student.getUsername());
			studentDTO.setNickname(student.getNickname());
			studentDTO.setPhoneNumber(student.getPhoneNumber());
			studentDTO.setBirth(student.getBirth());

			studentDTO.setSchool(student.getSchool());
			studentDTO.setGrade(student.getGrade());

			// 학생에 대한 부모님 정보 추가
			List<ParentDTO> parentDTOs = new ArrayList<ParentDTO>();
			List<ParentOfStudent> parentOfStudents = parentOfStudentRepository.findAllByStudentname(student.getUsername()).get();
			for(int i = 0; i < parentOfStudents.size(); i++) {
				Parent parent = parentOfStudents.get(i).getParent();
				
				ParentDTO parentDTO = ParentDTO.builder()
											   .id(parent.getId())
											   .nickname(parent.getNickname())
											   .relationship(parentOfStudents.get(i).getRelationship())
											   .build();
				
				parentDTOs.add(parentDTO);
			}
			studentDTO.setParentDTOs(parentDTOs);
			
			return studentDTO;
		} catch (Exception e) {
			log.info("[Failed] e : " + e.getMessage());
		}
		return null;
	}

}
