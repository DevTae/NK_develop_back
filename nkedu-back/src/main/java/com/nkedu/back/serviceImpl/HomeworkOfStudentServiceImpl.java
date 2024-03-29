package com.nkedu.back.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nkedu.back.api.HomeworkOfStudentService;
import com.nkedu.back.dto.HomeworkOfStudentDTO;
import com.nkedu.back.entity.HomeworkOfStudent.Status;
import com.nkedu.back.repository.HomeworkOfStudentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeworkOfStudentServiceImpl implements HomeworkOfStudentService {

	private final HomeworkOfStudentRepository homeworkOfStudentRepository; 
	
	@Override
	public HomeworkOfStudentDTO getHomeworkOfStudent(Long homeworkOfStudentId) {
		
		try {
			
		} catch (Exception e) {
			log.error("Failed: " + e.getMessage(),e);
		}
		
		return null;
		
	}

	@Override
	public List<HomeworkOfStudentDTO> getHomeworkOfStudents(Status filterOption) {
		
		try {	
			
		} catch (Exception e) {
			log.error("Failed: " + e.getMessage(),e);
		}
		
		return null;
	}

	@Override
	public HomeworkOfStudentDTO createHomeworkOfStudent(HomeworkOfStudentDTO homeworkOfStudentDTO) {
		
		try {
			
			// 신규 등록만 가능하도록 진행
			// 처음엔 무조건 TODO 로 설정하도록.
			
		} catch (Exception e) {
			log.error("Failed: " + e.getMessage(),e);
		}
		
		return null;
	}
	
	@Override
	public HomeworkOfStudentDTO updateHomeworkOfStudent(HomeworkOfStudentDTO homeworkOfStudentDTO) {
		
		try {
			
			
		} catch (Exception e) {
			log.error("Failed: " + e.getMessage(),e);
		}
		
		return null;
	}

	@Override
	public boolean deleteHomeworkOfStudent(Long homeworkOfStudentId) {
		
		try {
			
		} catch (Exception e) {
			log.error("Failed: " + e.getMessage(),e);
		}
		
		return false;
	}

}
