package com.nkedu.back.serviceImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.nkedu.back.entity.*;
import com.nkedu.back.exception.errorCode.UserErrorCode;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


import com.nkedu.back.api.ParentService;
import com.nkedu.back.entity.ParentOfStudent.Relationship;
import com.nkedu.back.dto.PageDTO;
import com.nkedu.back.dto.ParentDTO;
import com.nkedu.back.dto.ParentOfStudentDTO;
import com.nkedu.back.dto.StudentDTO;
import com.nkedu.back.repository.ParentOfStudentRepository;
import com.nkedu.back.repository.ParentRepository;
import com.nkedu.back.repository.StudentRepository;

import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {

	private final ParentRepository parentRepository;
	private final StudentRepository studentRepository;
	private final ParentOfStudentRepository parentOfStudentRepository;
	private final PasswordEncoder passwordEncoder;


	/**
	 * 부모 엔티티 CRUD 관련 API 구현 코드입니다.
	 * @author DevTae
	 */

	@Transactional
	@Override
	public boolean createParent(ParentDTO parentDTO) {

		Parent parent = null;
		boolean isSaved = false;

		try{
			if (!ObjectUtils.isEmpty(parentRepository.findOneByUsername(parentDTO.getUsername()))) {
				throw new RuntimeException("이미 가입되어 있는 유저입니다.");
//				throw new CustomException(UserErrorCode.DUPLICATE_USERNAME)
			}

			System.out.println("getUserName: " + parentDTO.getUsername());

			Set<Authority> authorities = new HashSet<Authority>();

			Authority authority_user = Authority.builder()
					.authorityName("ROLE_USER")
					.build();
			authorities.add(authority_user);

			Authority authority_parent = Authority.builder()
					.authorityName("ROLE_PARENT")
					.build();
			authorities.add(authority_parent);

			parent = (Parent) Parent.builder()
					.username(parentDTO.getUsername())
					.password(passwordEncoder.encode(parentDTO.getPassword()))
					.nickname(parentDTO.getNickname())
					.birth(parentDTO.getBirth())
					.phoneNumber(parentDTO.getPhoneNumber())
					.authorities(authorities)
					.created(new Timestamp(System.currentTimeMillis()))
					.activated(true)
					.build();

			parent = parentRepository.save(parent);
			isSaved = true; // 중도 저장 여부 확인

			// 학생에 대한 정보가 있을 시, 연동 진행
			Relationship relationship = null;
			if(!ObjectUtils.isEmpty(parentDTO.getRelationship())) {
				relationship = parentDTO.getRelationship();
			}
			if(!ObjectUtils.isEmpty(parentDTO.getStudentIds())) {
				for(Long studentId : parentDTO.getStudentIds()) {
					Student student = studentRepository.findById(studentId).get();
					this.createParentOfStudent(parent, student, relationship != null ? relationship : Relationship.NOK);
				}
			}

			return true;

		} catch(Exception e) {

			// 자식이 없어서 등록 완료에 실패한 경우, 등록된 부모 삭제 진행
			if(isSaved && parent != null)
				parentRepository.delete(parent);

			log.error("Failed: " + e.getMessage(),e);
        }
        return false;
	}

	@Override
	public boolean deleteByUsername(String username) {
		try {
			Parent parent = parentRepository.findOneByUsername(username).get();
			parent.setActivated(false);
			parentRepository.save(parent);

			return true;
		} catch (Exception e) {
			log.info("[Failed] e : " + e.getMessage());
		}
		return false;
	}

	@Override
	public boolean deletesById(ParentDTO parentDTO) {
		try{

			for(Long parent_id : parentDTO.getParentIds()){

				Optional<Parent> optionalParent = parentRepository.findById(parent_id);
				if (optionalParent.isEmpty()) {
					continue;
				}

				Parent parent = optionalParent.get();

				parent.setActivated(false);
				parentRepository.save(parent);
			}
			return true;
		} catch (Exception e){
			log.info("Failed e : " + e.getMessage());
		}
		return false;
	}


	@Transactional
	@Override
	public boolean updateParent(String username, ParentDTO parentDTO) {
		try {
			Parent searchedParent = parentRepository.findOneByUsername(username).get();

			if(ObjectUtils.isEmpty(searchedParent)){
				return false;
//				throw new CustomException(UserErrorCode.USER_NOT_FOUND)
			}

			if(!ObjectUtils.isEmpty(parentDTO.getUsername()))
				searchedParent.setUsername(parentDTO.getUsername());
			if(!ObjectUtils.isEmpty(parentDTO.getPassword()))
				searchedParent.setPassword(parentDTO.getPassword());
			if(!ObjectUtils.isEmpty(parentDTO.getNickname()))
				searchedParent.setNickname(parentDTO.getNickname());
			if(!ObjectUtils.isEmpty(parentDTO.getPhoneNumber()))
				searchedParent.setPhoneNumber(parentDTO.getPhoneNumber());
			if(!ObjectUtils.isEmpty(parentDTO.getBirth()))
				searchedParent.setBirth(parentDTO.getBirth());

			parentRepository.save(searchedParent);

			// 학생에 대한 정보가 있을 시, 연동 진행
			Relationship relationship = null;
			if(!ObjectUtils.isEmpty(parentDTO.getRelationship())) {
				relationship = parentDTO.getRelationship();
			}
			if(parentDTO.getStudentIds() != null) {
				// 기존 학생-부모 관계 모두 삭제
				List<ParentOfStudent> parentOfStudents = parentOfStudentRepository.findAllByParentname(searchedParent.getUsername()).get();
				parentOfStudentRepository.deleteAll(parentOfStudents);

				// studentIds 로 등록된 모든 학생들 관계 주입
				for(Long studentId : parentDTO.getStudentIds()) {
					Student student = studentRepository.findById(studentId).get();
					this.createParentOfStudent(searchedParent, student, relationship != null ? relationship : Relationship.NOK);
				}
			}

			return true;
		} catch (Exception e) {
			log.info("[Failed] e : " + e.getMessage());
		}

		return false;
	}

	@Override
	public List<ParentDTO> getParents() {
		try {
			List<ParentDTO> parentDTOs = new ArrayList<>();

			List<Parent> parents = parentRepository.findAll();

			for(Parent parent : parents) {
				ParentDTO parentDTO = new ParentDTO();
				parentDTO.setId(parent.getId());
				parentDTO.setUsername(parent.getUsername());
				parentDTO.setNickname(parent.getNickname());
				parentDTO.setPhoneNumber(parent.getPhoneNumber());
				parentDTO.setBirth(parent.getBirth());

				// 부모님에 대한 학생 정보 추가
				List<StudentDTO> studentDTOs = new ArrayList<StudentDTO>();
				List<ParentOfStudent> parentOfStudents = parentOfStudentRepository.findAllByParentname(parent.getUsername()).get();
				for(int i = 0; i < parentOfStudents.size(); i++) {
					Student student = parentOfStudents.get(i).getStudent();

					StudentDTO studentDTO = StudentDTO.builder()
							.id(student.getId())
							.nickname(student.getNickname())
							.build();

					studentDTOs.add(studentDTO);
				}
				parentDTO.setStudentDTOs(studentDTOs);
				if(parentOfStudents.size() > 0)
					parentDTO.setRelationship(parentOfStudents.get(0).getRelationship()); // 최상단의 relationship 바탕으로 반환

				parentDTOs.add(parentDTO);
			}

			return parentDTOs;
		} catch(Exception e) {
			log.info("[Failed] e : " + e.getMessage());
		}

		return null;
	}

	@Override
	public PageDTO<ParentDTO> getParentsByKeyword(Integer page, String keyword) {
		try {

			PageDTO<ParentDTO> pageDTO = new PageDTO<>();
			List<ParentDTO> parentDTOs = new ArrayList<>();

			// 정렬 기준
			List<Sort.Order> sorts = new ArrayList<>();
			sorts.add(Sort.Order.asc("nickname"));
			Pageable pageable = PageRequest.of(page, 16, Sort.by(sorts));

			// Page 조회
			Page<Parent> pageOfParent = parentRepository.findAllByParentName(pageable,keyword);

			pageDTO.setCurrentPage(pageOfParent.getNumber() + 1);
			pageDTO.setTotalPage(pageOfParent.getTotalPages());

			for(Parent parent : pageOfParent.getContent()) {
				ParentDTO parentDTO = new ParentDTO();
				parentDTO.setId(parent.getId());
				parentDTO.setUsername(parent.getUsername());
				parentDTO.setNickname(parent.getNickname());
				parentDTO.setPhoneNumber(parent.getPhoneNumber());
				parentDTO.setBirth(parent.getBirth());

				// 부모님에 대한 학생 정보 추가
				List<StudentDTO> studentDTOs = new ArrayList<StudentDTO>();
				List<ParentOfStudent> parentOfStudents = parentOfStudentRepository.findAllByParentname(parent.getUsername()).get();
				for(int i = 0; i < parentOfStudents.size(); i++) {
					Student student = parentOfStudents.get(i).getStudent();

					StudentDTO studentDTO = StudentDTO.builder()
							.id(student.getId())
							.nickname(student.getNickname())
							.build();

					studentDTOs.add(studentDTO);
				}
				parentDTO.setStudentDTOs(studentDTOs);
				if(parentOfStudents.size() > 0)
					parentDTO.setRelationship(parentOfStudents.get(0).getRelationship()); // 최상단의 relationship 바탕으로 반환

				parentDTOs.add(parentDTO);
			}

			pageDTO.setResults(parentDTOs);

			return pageDTO;
		} catch(Exception e) {
			log.info("[Failed] e : " + e.getMessage());
		}

		return null;
	}

	@Override
	public ParentDTO getParentByUsername(String username) {

		try {
			Parent parent = parentRepository.findOneByUsername(username).get();

			ParentDTO parentDTO = new ParentDTO();
			parentDTO.setId(parent.getId());
			parentDTO.setUsername(parent.getUsername());
			parentDTO.setNickname(parent.getNickname());
			parentDTO.setPhoneNumber(parent.getPhoneNumber());
			parentDTO.setBirth(parent.getBirth());

			// 부모님에 대한 학생 정보 추가
			List<StudentDTO> studentDTOs = new ArrayList<StudentDTO>();
			List<ParentOfStudent> parentOfStudents = parentOfStudentRepository.findAllByParentname(parent.getUsername()).get();
			for(int i = 0; i < parentOfStudents.size(); i++) {
				Student student = parentOfStudents.get(i).getStudent();

				StudentDTO studentDTO = StudentDTO.builder()
						.id(student.getId())
						.nickname(student.getNickname())
						.build();

				studentDTOs.add(studentDTO);
			}
			parentDTO.setStudentDTOs(studentDTOs);
			if(parentOfStudents.size() > 0)
				parentDTO.setRelationship(parentOfStudents.get(0).getRelationship()); // 최상단의 relationship 바탕으로 반환

			return parentDTO;

		} catch (Exception e) {
			log.info("[Failed] e : " + e.getMessage());
		}

		return null;
	}


	/**
	 * 학생 및 부모 사이의 관계 정의를 위한 서비스 구현 코드입니다.
	 *
	 * @author DevTae
	 */

	@Transactional
	@Override
	public ParentOfStudentDTO createParentOfStudent(ParentOfStudentDTO parentOfStudentDTO) {

		try {
			String parentname = parentOfStudentDTO.getParentDTO().getUsername();
			String studentname = parentOfStudentDTO.getStudentDTO().getUsername();
			Relationship relationship = parentOfStudentDTO.getRelationship();

			Optional<ParentOfStudent> optionalParentOfStudent = parentOfStudentRepository.findOneByParentnameAndStudentname(parentname, studentname);

			// 중복 등록 방지를 위한 조건문
			if (!ObjectUtils.isEmpty(optionalParentOfStudent)) {
				log.info("[Failed] Duplicated ParentOfStudent record occured. Skip it.");
				return null;
			}

			ParentOfStudent parentOfStudent = ParentOfStudent.builder()
					.parent(parentRepository.findOneByUsername(parentname).get())
					.student(studentRepository.findOneByUsername(studentname).get())
					.relationship(relationship)
					.build();

			parentOfStudentRepository.save(parentOfStudent);

			return parentOfStudentDTO;

		} catch (Exception e) {
			log.info("[Failed] e : " + e.getMessage());
		}

		return null;
	}

	// 서비스 내부에서 사용하는 함수, Parent, Student, Relationship 정보를 활용하여 부모-자식 간의 관계를 형성함.
	@Transactional
	public void createParentOfStudent(Parent parent, Student student, Relationship relationship) {
		try {
			String parentname = parent.getUsername();
			String studentname = student.getUsername();

			Optional<ParentOfStudent> optionalParentOfStudent = parentOfStudentRepository.findOneByParentnameAndStudentname(parentname, studentname);

			// 중복 등록 방지를 위한 조건문
			if (!ObjectUtils.isEmpty(optionalParentOfStudent)) {
				log.info("[Failed] Duplicated ParentOfStudent record occured. Skip it.");
				return;
			}

			ParentOfStudent parentOfStudent = ParentOfStudent.builder()
					.parent(parent)
					.student(student)
					.relationship(relationship)
					.build();

			parentOfStudentRepository.save(parentOfStudent);

		} catch (Exception e) {
			log.info("[Failed] e : " + e.getMessage());
		}
	}

	@Override
	public List<ParentOfStudentDTO> getParentOfStudentsByParentname(String parentname) {

		try {
			List<ParentOfStudent> parentOfStudents = parentOfStudentRepository.findAllByParentname(parentname).get();

			List<ParentOfStudentDTO> parentOfStudentDTOs = new ArrayList<>();

			for(ParentOfStudent parentOfStudent : parentOfStudents) {
				String parentname_each = parentOfStudent.getParent().getUsername();
				String studentname_each = parentOfStudent.getStudent().getUsername();
				Relationship relationship = parentOfStudent.getRelationship();

				ParentOfStudentDTO parentOfStudentDTO = ParentOfStudentDTO.builder()
						.parentDTO(ParentDTO.builder().username(parentname_each).build())
						.studentDTO(StudentDTO.builder().username(studentname_each).build())
						.relationship(relationship)
						.build();

				parentOfStudentDTOs.add(parentOfStudentDTO);
			}

			return parentOfStudentDTOs;

		} catch (Exception e) {
			log.info("[Failed] e : " + e.getMessage());
		}

		return null;
	}

	@Transactional
	@Override
	public boolean deleteParentOfStudent(ParentOfStudentDTO parentOfStudentDTO) {

		try {
			String parentname = parentOfStudentDTO.getParentDTO().getUsername();
			String studentname = parentOfStudentDTO.getStudentDTO().getUsername();

			ParentOfStudent parentOfStudent = parentOfStudentRepository.findOneByParentnameAndStudentname(parentname, studentname).get();

			parentOfStudentRepository.delete(parentOfStudent);

			return true;

		} catch (Exception e) {
			log.info("[Failed] e : " + e.getMessage());
		}

		return false;
	}

}
