package com.nkedu.back.serviceImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.nkedu.back.api.HomeworkOfStudentService;
import com.nkedu.back.dto.FileDataDTO;
import com.nkedu.back.dto.HomeworkOfStudentDTO;
import com.nkedu.back.entity.FileData;
import com.nkedu.back.entity.Homework;
import com.nkedu.back.entity.HomeworkOfStudent;
import com.nkedu.back.entity.HomeworkOfStudent.Status;
import com.nkedu.back.entity.Student;
import com.nkedu.back.exception.errorCode.HomeworkErrorCode;
import com.nkedu.back.exception.errorCode.UserErrorCode;
import com.nkedu.back.exception.exception.CustomException;
import com.nkedu.back.repository.FileDataRepository;
import com.nkedu.back.repository.HomeworkOfStudentRepository;
import com.nkedu.back.repository.HomeworkRepository;
import com.nkedu.back.repository.StudentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeworkOfStudentServiceImpl implements HomeworkOfStudentService {

	private final HomeworkOfStudentRepository homeworkOfStudentRepository;
	private final HomeworkRepository homeworkRepository;
	private final StudentRepository studentRepository;
	private final FileDataRepository fileDataRepository;
	
	@Override
	public HomeworkOfStudentDTO getHomeworkOfStudent(Long homeworkOfStudentId) {
		
		try {
			HomeworkOfStudent hos = homeworkOfStudentRepository.findById(homeworkOfStudentId).get();
			
			List<Long> list_of_fileIds = new ArrayList<Long>();
			
			for(FileData fileData : hos.getFiles()) {
				list_of_fileIds.add(fileData.getId());
			}
			
			HomeworkOfStudentDTO hosDTO = HomeworkOfStudentDTO.builder()
															  .id(homeworkOfStudentId)
															  .homeworkId(hos.getHomework().getId())
															  .studentId(hos.getStudent().getId())
															  .studentName(hos.getStudent().getNickname())
															  .status(hos.getStatus())
															  .feedback(hos.getFeedback())
															  .created(hos.getCreated())
															  .updated(hos.getUpdated())
															  .fileIds(list_of_fileIds)
															  .stopwatch(hos.getStopwatch()) // 스탑워치 정보
															  .build();
			
			return hosDTO;
			
		} catch (Exception e) {
			log.error("Failed: " + e.getMessage(),e);
		}
		
		return null;
		
	}

	@Override
	public List<HomeworkOfStudentDTO> getHomeworkOfStudents(Long homeworkId, Status filterOption) {
		
		try {	
			List<HomeworkOfStudent> list_of_hos = homeworkOfStudentRepository.findAllByHomeworkId(homeworkId);
			
			List<HomeworkOfStudentDTO> list_of_hosDTO = new ArrayList<HomeworkOfStudentDTO>();
			
			for(HomeworkOfStudent hos : list_of_hos) {
				
				// 필터링 기능 수행
				if(filterOption != null && !filterOption.equals(hos.getStatus()))
					continue;
				
				List<FileDataDTO> fileDTOs = new ArrayList<FileDataDTO>();
				
				for(FileData fileData : hos.getFiles()) {
					fileDTOs.add(FileDataDTO.builder()
											.id(fileData.getId())
											//.name(fileData.getName())
											//.path(fileData.getPath())
											//.type(fileData.getType())
											.build());
				}
				
				HomeworkOfStudentDTO hosDTO = HomeworkOfStudentDTO.builder()
																  .id(hos.getId())
																  .homeworkId(hos.getHomework().getId())
																  .studentId(hos.getStudent().getId())
																  .studentName(hos.getStudent().getNickname())
																  .status(hos.getStatus())
																  //.feedback(hos.getFeedback()) // 세부 조회에서 반환하도록
																  .created(hos.getCreated())
																  .updated(hos.getUpdated())
																  //.files(fileDTOs) // 세부 조회에서 반환하도록
																  .build();
				
				list_of_hosDTO.add(hosDTO);
			}
			
			return list_of_hosDTO;
			
		} catch (Exception e) {
			log.error("Failed: " + e.getMessage(),e);
		}
		
		return null;
	}
	
	@Override
	public List<HomeworkOfStudentDTO> getHomeworkOfStudents(Long homeworkId, String username, Status filterOption) {
		
		try {
			List<HomeworkOfStudent> list_of_hos = homeworkOfStudentRepository.findAllByHomeworkIdAndUsername(homeworkId, username);
			
			System.out.println("size is " + list_of_hos.size());
			
			
			List<HomeworkOfStudentDTO> list_of_hosDTO = new ArrayList<HomeworkOfStudentDTO>();
			
			for(HomeworkOfStudent hos : list_of_hos) {
				
				// 필터링 기능 수행
				if(filterOption != null && !filterOption.equals(hos.getStatus()))
					continue;
				
				/*
				List<FileDataDTO> fileDTOs = new ArrayList<FileDataDTO>();
				
				for(FileData fileData : hos.getFiles()) {
					fileDTOs.add(FileDataDTO.builder()
											.id(fileData.getId())
											//.name(fileData.getName())
											//.path(fileData.getPath())
											//.type(fileData.getType())
											.build());
				}
				*/
				
				HomeworkOfStudentDTO hosDTO = HomeworkOfStudentDTO.builder()
																  .id(hos.getId())
																  .homeworkId(hos.getHomework().getId())
																  .studentId(hos.getStudent().getId())
																  .studentName(hos.getStudent().getNickname())
																  .status(hos.getStatus())
																  //.feedback(hos.getFeedback()) // 세부 조회에서 반환
																  .created(hos.getCreated())
																  .updated(hos.getUpdated()) // 세부 조회에서 반환
																  //.files(fileDTOs)
																  .build();
				
				list_of_hosDTO.add(hosDTO);
			}
			
			return list_of_hosDTO;
			
		} catch (Exception e) {
			log.error("Failed: " + e.getMessage(),e);
		}
		
		return null;
	}

	@Override
	public HomeworkOfStudentDTO createHomeworkOfStudent(HomeworkOfStudentDTO homeworkOfStudentDTO) { // , String username (검증)
		
		try {
			
			boolean imageUploaded = false;
			
			Long homeworkId = homeworkOfStudentDTO.getHomeworkId();
			Long studentId = homeworkOfStudentDTO.getStudentId();
			
			// 신규 등록만 가능하도록 진행 (이미 존재한다면 실패)
			List<HomeworkOfStudent> list_of_hos = homeworkOfStudentRepository.findAllByHomeworkIdAndStudentId(homeworkId, studentId);
			if(list_of_hos.size() > 0) {
				throw new CustomException(HomeworkErrorCode.DUPLICATE_HOMEWORK_OF_STUDENT);
			}
			
			// 숙제 및 학생에 대한 확인
			Optional<Homework> homework_optional = homeworkRepository.findById(homeworkId);
			if(homework_optional.isEmpty())
				throw new CustomException(HomeworkErrorCode.HOMEWORK_NOT_FOUND);
			Homework homework = homework_optional.get();

			Optional<Student> student_optional = studentRepository.findById(studentId);
			if(student_optional.isEmpty())
				throw new CustomException(UserErrorCode.USER_NOT_FOUND);
			Student student = student_optional.get();

			// 생성 시간 입력
			Timestamp now = new Timestamp(System.currentTimeMillis());
			
			// 파일 엔티티 불러오기
			List<FileData> list_of_fileDatas = new ArrayList<FileData>();
			
			if(!ObjectUtils.isEmpty(homeworkOfStudentDTO.getFileIds())) {
				
				// image 업로드부터 진행했다면, 바로 제출로 인정해줌.
				imageUploaded = true;
				
				for(Long id : homeworkOfStudentDTO.getFileIds()) {
					FileData fileData = fileDataRepository.findById(id).get();
					
					list_of_fileDatas.add(fileData);
				}
			}
			
			// 새로운 HomeworkOfStudent 생성 (/homework/{id}/submit/list 를 바탕으로 전체 학생에 대한 현황 볼 수 있도록 함)
			HomeworkOfStudent hos = HomeworkOfStudent.builder()
													 .homework(homework)
													 .student(student)
													 .status(imageUploaded ? Status.SUBMIT : Status.TODO)
													 .feedback("") // 기본 속성 값
													 .created(now)
													 .updated(now)
													 .files(list_of_fileDatas)
													 .stopwatch(0)
													 .build();
			
			hos = homeworkOfStudentRepository.save(hos);
			
			// DTO 반환 진행
			return HomeworkOfStudentDTO.builder()
									   .id(hos.getId())
									   .homeworkId(hos.getHomework().getId())
									   .studentId(hos.getStudent().getId())
									   .studentName(hos.getStudent().getNickname())
									   .status(hos.getStatus())
									   .feedback(hos.getFeedback())
									   .created(hos.getCreated())
									   .updated(hos.getUpdated())
									   .fileIds(homeworkOfStudentDTO.getFileIds())
									   .stopwatch(hos.getStopwatch())
									   .build();									   
									   
		} catch (Exception e) {
			log.error("Failed: " + e.getMessage(),e);
			
			throw e;
		}
	}
	
	@Override
	public HomeworkOfStudentDTO updateHomeworkOfStudent(HomeworkOfStudentDTO homeworkOfStudentDTO) {
		
		try {
			
			HomeworkOfStudent hos = homeworkOfStudentRepository.findById(homeworkOfStudentDTO.getId()).get();
			
			if(!ObjectUtils.isEmpty(homeworkOfStudentDTO.getFeedback()))
				hos.setFeedback(homeworkOfStudentDTO.getFeedback());
				
			if(!ObjectUtils.isEmpty(homeworkOfStudentDTO.getStatus()))
				hos.setStatus(homeworkOfStudentDTO.getStatus());
			
			if(!ObjectUtils.isEmpty(homeworkOfStudentDTO.getStopwatch()))
				hos.setStopwatch(homeworkOfStudentDTO.getStopwatch());
				
			if(!ObjectUtils.isEmpty(homeworkOfStudentDTO.getFileIds())) {
				List<FileData> list_of_fileData = new ArrayList<FileData>();
				
				for(Long id : homeworkOfStudentDTO.getFileIds()) {
					FileData fileData = fileDataRepository.findById(id).get();
					
					list_of_fileData.add(fileData);
				}
				
				hos.setFiles(list_of_fileData);
			}
			
			homeworkOfStudentRepository.save(hos);
			
			// DTO 반환 진행
			return HomeworkOfStudentDTO.builder()
									   .homeworkId(hos.getHomework().getId())
									   .studentId(hos.getStudent().getId())
									   .studentName(hos.getStudent().getNickname())
									   .status(hos.getStatus())
									   .feedback(hos.getFeedback())
									   .created(hos.getCreated())
									   .updated(hos.getUpdated())
									   .fileIds(homeworkOfStudentDTO.getFileIds())
									   .stopwatch(hos.getStopwatch())
									   .build();		
			
		} catch (Exception e) {
			log.error("Failed: " + e.getMessage(),e);
			
			throw e;
		}
	}
	
	@Override
	public HomeworkOfStudentDTO updateHomeworkOfStudent(HomeworkOfStudentDTO homeworkOfStudentDTO, String username) {
		
		try {
			
			Long homeworkId = homeworkOfStudentDTO.getHomeworkId();
			List<HomeworkOfStudent> list_of_hos = homeworkOfStudentRepository.findAllByHomeworkIdAndUsername(homeworkId, username);
			if(list_of_hos == null || list_of_hos.size() != 1) return null; // 숙제 제출 엔티티 비정상 상황에서 null 반환 (internal server error)
			
			// 검색된 숙제 제출 엔티티
			HomeworkOfStudent hos = list_of_hos.get(0);
			
			if(!ObjectUtils.isEmpty(homeworkOfStudentDTO.getFeedback()))
				hos.setFeedback(homeworkOfStudentDTO.getFeedback());
				
			if(!ObjectUtils.isEmpty(homeworkOfStudentDTO.getStatus()))
				hos.setStatus(homeworkOfStudentDTO.getStatus());
			
			if(!ObjectUtils.isEmpty(homeworkOfStudentDTO.getStopwatch()))
				hos.setStopwatch(homeworkOfStudentDTO.getStopwatch());
				
			if(!ObjectUtils.isEmpty(homeworkOfStudentDTO.getFileIds())) {
				List<FileData> list_of_fileData = new ArrayList<FileData>();
				
				for(Long id : homeworkOfStudentDTO.getFileIds()) {
					FileData fileData = fileDataRepository.findById(id).get();
					
					list_of_fileData.add(fileData);
				}
				
				hos.setFiles(list_of_fileData);
			}
			
			homeworkOfStudentRepository.save(hos);
			
			// DTO 반환 진행
			return HomeworkOfStudentDTO.builder()
									   .homeworkId(hos.getHomework().getId())
									   .studentId(hos.getStudent().getId())
									   .studentName(hos.getStudent().getNickname())
									   .status(hos.getStatus())
									   .feedback(hos.getFeedback())
									   .created(hos.getCreated())
									   .updated(hos.getUpdated())
									   .fileIds(homeworkOfStudentDTO.getFileIds())
									   .stopwatch(hos.getStopwatch())
									   .build();		
			
		} catch (Exception e) {
			log.error("Failed: " + e.getMessage(),e);
			
			throw e;
		}
	}

	@Override
	public Boolean deleteHomeworkOfStudent(Long homeworkOfStudentId) {
		
		try {
			
			HomeworkOfStudent hos = homeworkOfStudentRepository.findById(homeworkOfStudentId).get();
			
			homeworkOfStudentRepository.delete(hos);
			
			return true;
			
		} catch (Exception e) {
			log.error("Failed: " + e.getMessage(),e);
		}
		
		return false;
	}
	
	
	@Override
	public Double getStopwatch(Long homeworkOfStudentId) throws Exception {
		try {
			
			HomeworkOfStudent hos = homeworkOfStudentRepository.findById(homeworkOfStudentId).get();
			
			double result = hos.getStopwatch();
			
			return result;
			
		} catch (Exception e) {
			log.error("Failed: " + e.getMessage(),e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	@Override
	public Double getStopwatch(Long homeworkId, String username) throws Exception {
		try {
			
			List<HomeworkOfStudent> list_of_hos = homeworkOfStudentRepository.findAllByHomeworkIdAndUsername(homeworkId, username);
			if(list_of_hos == null || list_of_hos.size() != 1) return null; // 숙제 제출 엔티티 비정상 상황에서 null 반환 (internal server error)
			
			// 검색된 숙제 제출 엔티티
			HomeworkOfStudent hos = list_of_hos.get(0);
			
			Double result = hos.getStopwatch();
			
			return result;
			
		} catch (Exception e) {
			log.error("Failed: " + e.getMessage(),e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	@Override
	public Boolean setStopwatch(Long homeworkOfStudentId, HomeworkOfStudentDTO homeworkOfStudentDTO) {
		try {
			if(ObjectUtils.isEmpty(homeworkOfStudentDTO.getStopwatch()))
				return false;
			
			Double value = homeworkOfStudentDTO.getStopwatch();
			
			HomeworkOfStudent hos = homeworkOfStudentRepository.findById(homeworkOfStudentId).get();
			hos.setStopwatch(value);
			homeworkOfStudentRepository.save(hos);
			
			return true;
			
		} catch (Exception e) {
			log.error("Failed: " + e.getMessage(),e);
		}

		return false;
	}
	
	@Override
	public Boolean setStopwatch(Long homeworkId, String username, HomeworkOfStudentDTO homeworkOfStudentDTO) {
		try {
			if(ObjectUtils.isEmpty(homeworkOfStudentDTO.getStopwatch()))
				return false;
			
			Double value = homeworkOfStudentDTO.getStopwatch();
			
			List<HomeworkOfStudent> list_of_hos = homeworkOfStudentRepository.findAllByHomeworkIdAndUsername(homeworkId, username);
			if(list_of_hos == null || list_of_hos.size() != 1) return null; // 숙제 제출 엔티티 비정상 상황에서 null 반환 (internal server error)
			
			// 검색된 숙제 제출 엔티티
			HomeworkOfStudent hos = list_of_hos.get(0);
			hos.setStopwatch(value);
			
			homeworkOfStudentRepository.save(hos);
			
			return true;
			
		} catch (Exception e) {
			log.error("Failed: " + e.getMessage(),e);
		}

		return false;
	}
	

}
