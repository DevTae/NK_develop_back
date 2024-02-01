package com.nkedu.back.serviceImpl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.nkedu.back.api.StudentService;
import com.nkedu.back.model.School;
import com.nkedu.back.model.Student;
import com.nkedu.back.model.StudentDTO;
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
	
    // ��� �л�  ���� ����Ʈ ��ȸ
    public List<StudentDTO> getAllStudents() {
    	try {
    		// ������Ʈ�� studentDTO ���� ��
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
				
				studentDTOs.add(studentDTO);
			}
			return studentDTOs;
		} catch(Exception e) {
			log.info("[Failed] e : " + e.getMessage());
		}
		return null;    
	}
    
	// �л� ���� ���� 
	public boolean createStudent(StudentDTO studentDTO) {		
		try {
			//1. ��û�� �б��� ���� �б� DB�� �����ϴ��� ���� �Ǵ��ϴ� Logic
			
			// ��û���� School ��ü
			School postedSchool = studentDTO.getSchool();
			
			// ��û���� School ��ü�� ������� ��� 
			if(postedSchool == null || postedSchool.getId() == null || postedSchool.getSchoolName()==null) {
				throw new IllegalArgumentException("��û�Ͻ� �б��� ������ ������� �ʽ��ϴ�.");
			}
			
			// ��û���� School ��ü�� ���� schoolRepo�� �����Ѵٸ�, searchedSchool ����� �Ҵ� 
			// ���ٸ�, existingSchool = null�� �Ҵ� 
			School searchedSchool = schoolRepository.findById(postedSchool.getId()).orElse(null);
			
			// 1-1. �ش� id�� school�� �������� ���� �� 			
			// if (searchedSchool == null) {
			//	throw new NullPointerException("��û�Ͻ� " + postedSchool.getSchoolName() +"�� �߸��� ��û�Դϴ�.");
			//}
			// 1�� ��� �ڵ����� null ���̸�  nullpointer�� �˾Ƽ� ó��
			
			// 2-1. id�� �����ص�, (�ش� id�� �б� �̸� != ��û�� �б��� �̸�) �� ��� 
			if(!searchedSchool.getSchoolName().equals(postedSchool.getSchoolName())) {
				throw new IllegalArgumentException("��û�Ͻ� id�� �ش��ϴ� �б��� ��û�� " + postedSchool.getSchoolName() +"�� �ٸ� �б��Դϴ�.");
			}


			//2. �л� ���� 
			Student student = new Student();
			
			student.setUsername(studentDTO.getUsername());
			student.setPassword(studentDTO.getPassword());
			student.setNickname(studentDTO.getNickname());
			student.setCreated(new Timestamp(System.currentTimeMillis()));
			student.setPhoneNumber(studentDTO.getPhoneNumber());
			student.setBirth(studentDTO.getBirth());


			student.setSchool(postedSchool); //or searchedSchool?
			student.setGrade(studentDTO.getGrade());
			

			studentRepository.save(student);
			
			return true;
		} catch (IllegalArgumentException e) {
	        log.error("Failed: " + e.getMessage(), e);
	    } catch (Exception e) {
	        log.error("Failed: " + e.getMessage(), e);
		}
		return false;
	}
	
    // �л� ���� ����
    public boolean deleteStudentById(Long studentId) {
        try{
        	studentRepository.deleteById(studentId);
        	return true;
        } catch (Exception e){
    		log.info("failed e : " + e.getMessage());
        }
        return false;
    }
    
    // �л� ���� ����
	// �б� ���� API�� ���� ������ �� �� (ex. ������)
	// grade �� �ų� �ڵ����� �ø���.. ��� �ؾ����� ���
	public boolean updateStudent(Long studentId, StudentDTO studentDTO) {
		try {
			Student searchedStudent = studentRepository.findById(studentId).get();
			
			if(ObjectUtils.isEmpty(searchedStudent))
				return false;
			
			// ��û ���� �л� �̸����� ������Ʈ 
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
			
			studentRepository.save(searchedStudent);
			return true;
		} catch (Exception e) {
			log.info("[Failed] e : " + e.getMessage());
		}
		return false;
	}
	
    
    // Ư�� �л� ���� ���� ��ȸ
    // � �������� �ѱ����� ���� �ǵ���� ���� API �߰��� ����� ��
    public StudentDTO getStudentById(Long studentId) {
		try {
			Student student = studentRepository.findById(studentId).get();
			
			// Ư�� �л��� ���� ������ ���� DTO ����
			StudentDTO studentDTO = new StudentDTO();
			
			studentDTO.setId(student.getId());
			studentDTO.setUsername(student.getUsername());
			studentDTO.setNickname(student.getNickname());
			studentDTO.setPhoneNumber(student.getPhoneNumber());
			studentDTO.setBirth(student.getBirth());

			studentDTO.setSchool(student.getSchool());
			studentDTO.setGrade(student.getGrade());

			return studentDTO;
		} catch (Exception e) {
			log.info("[Failed] e : " + e.getMessage());
		}
		return null;    
	}
	
    
    // �л� ���� �������� ��ȸ -> ���� ������ ������ � ������ ���� �������� ���� �߰� ���� ��
    
}