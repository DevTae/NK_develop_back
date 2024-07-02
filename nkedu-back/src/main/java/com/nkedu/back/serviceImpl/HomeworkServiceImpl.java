package com.nkedu.back.serviceImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.nkedu.back.api.HomeworkService;
import com.nkedu.back.dto.ClassroomDTO;
import com.nkedu.back.dto.HomeworkDTO;
import com.nkedu.back.dto.PageDTO;
import com.nkedu.back.dto.ParentDTO;
import com.nkedu.back.dto.TeacherDTO;
import com.nkedu.back.entity.Classroom;
import com.nkedu.back.entity.Homework;
import com.nkedu.back.entity.HomeworkOfStudent;
import com.nkedu.back.entity.Parent;
import com.nkedu.back.entity.HomeworkOfStudent.Status;
import com.nkedu.back.entity.Teacher;
import com.nkedu.back.exception.errorCode.ClassErrorCode;
import com.nkedu.back.exception.errorCode.HomeworkErrorCode;
import com.nkedu.back.exception.errorCode.UserErrorCode;
import com.nkedu.back.exception.exception.CustomException;
import com.nkedu.back.repository.ClassroomRepository;
import com.nkedu.back.repository.HomeworkOfStudentRepository;
import com.nkedu.back.repository.HomeworkRepository;
import com.nkedu.back.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeworkServiceImpl implements HomeworkService {

	private final HomeworkRepository homeworkRepository;
	private final HomeworkOfStudentRepository homeworkOfStudentRepository;
	private final ClassroomRepository classroomRepository;
	private final TeacherRepository teacherRepository;
	
	// 현재 사용X
	@Override
	public List<HomeworkDTO> getHomeworks(Long classId) {
		
		try {
			List<Homework> homeworks = homeworkRepository.findAllByClassroomId(classId);
			List<HomeworkDTO> homeworkDTOs = new ArrayList<HomeworkDTO>();
			
			for(Homework homework : homeworks) {
				homeworkDTOs.add(HomeworkDTO.builder()
										    .id(homework.getId())
										    .title(homework.getTitle())
										    .teacherDTO(TeacherDTO.builder()
										 			   .id(homework.getTeacher().getId())
										 			   .nickname(homework.getTeacher().getNickname())
										 		       .build())
										    .classroomDTO(ClassroomDTO.builder()
									    			 .id(homework.getClassroom().getId())
									    			 .classname(homework.getClassroom().getClassname())
									    			 .build())
										    //.teacherId(homework.getTeacher().getId())
										    .created(homework.getCreated())
										    .updated(homework.getUpdated())
										    .deadline(homework.getDeadline())
										    .build());
			}
			
			return homeworkDTOs;
		} catch (Exception e) {
			log.info("Failed e : " + e.getMessage());
		}
		return null;
	}
	
	@Override
	public List<HomeworkDTO> getHomeworks(Long classId, String username) {
		
		try {
			List<Homework> homeworks = homeworkRepository.findAllByClassroomId(classId);
			List<HomeworkDTO> homeworkDTOs = new ArrayList<HomeworkDTO>();
			
			for(Homework homework : homeworks) {
				
				// 학생에 대한 숙제 status 불러오기
				List<HomeworkOfStudent> homeworkOfStudents = homeworkOfStudentRepository.findAllByHomeworkIdAndUsername(homework.getId(), username);
				
				Status status;
				
				if (homeworkOfStudents.size() == 0) {
					status = Status.TODO;
				} else {
					status = homeworkOfStudents.get(0).getStatus();
				}
				
				homeworkDTOs.add(HomeworkDTO.builder()
										    .id(homework.getId())
										    .title(homework.getTitle())
										    .teacherDTO(TeacherDTO.builder()
										 			   .id(homework.getTeacher().getId())
										 			   .nickname(homework.getTeacher().getNickname())
										 		       .build())
										    .classroomDTO(ClassroomDTO.builder()
										    			 .id(homework.getClassroom().getId())
										    			 .classname(homework.getClassroom().getClassname())
										    			 .build())
										    //.teacherId(homework.getTeacher().getId())
										    .created(homework.getCreated())
										    .updated(homework.getUpdated())
										    .deadline(homework.getDeadline())
										    .status(status)
										    .build());
			}
			
			return homeworkDTOs;
		} catch (Exception e) {
			log.info("Failed e : " + e.getMessage());
		}
		return null;
	}
	
	@Override
	public PageDTO<HomeworkDTO> getHomeworks(Long classId, String username, Integer page, Status status) {
		
		try {
			
			PageDTO<HomeworkDTO> pageDTO = new PageDTO<>();
			List<HomeworkDTO> homeworkDTOs = new ArrayList<>();

			// 정렬 기준
			List<Sort.Order> sorts = new ArrayList<>();
			sorts.add(Sort.Order.desc("created"));
			Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(sorts));

			// Page 조회
			Page<Homework> pageOfHomework = null;
			
			if(status != null) {
				pageOfHomework = homeworkRepository.findAllByClassroomIdAndUsernameAndStatus(classId, username, status, pageable);
			} else {
				pageOfHomework = homeworkRepository.findAllByClassroomIdAndUsername(classId, username, pageable);
			}
			
			// Page 전체 정보 저장
			pageDTO.setCurrentPage(pageOfHomework.getNumber() + 1);
			pageDTO.setTotalPage(pageOfHomework.getTotalPages());
			
			for(Homework homework : pageOfHomework.getContent()) {
				
				// 학생에 대한 숙제 status 불러오기
				List<HomeworkOfStudent> homeworkOfStudents = homeworkOfStudentRepository.findAllByHomeworkIdAndUsername(homework.getId(), username);
				
				Status status_;
				
				if (homeworkOfStudents.size() == 0) {
					status_ = Status.TODO;
				} else {
					status_ = homeworkOfStudents.get(0).getStatus();
				}
				
				homeworkDTOs.add(HomeworkDTO.builder()
										    .id(homework.getId())
										    .title(homework.getTitle())
										    .teacherDTO(TeacherDTO.builder()
										 			   .id(homework.getTeacher().getId())
										 			   .nickname(homework.getTeacher().getNickname())
										 		       .build())
										    .classroomDTO(ClassroomDTO.builder()
									    			 .id(homework.getClassroom().getId())
									    			 .classname(homework.getClassroom().getClassname())
									    			 .build())
										    //.teacherId(homework.getTeacher().getId())
										    .created(homework.getCreated())
										    .updated(homework.getUpdated())
										    .deadline(homework.getDeadline())
										    .status(status_)
										    .build());
			}
			
			pageDTO.setResults(homeworkDTOs);
			
			return pageDTO;
			
		} catch (Exception e) {
			log.info("Failed e : " + e.getMessage());
		}
		return null;
	}
	
	@Override
	public PageDTO<HomeworkDTO> getHomeworks(String username, Integer page, Status status) {

		try {
			
			PageDTO<HomeworkDTO> pageDTO = new PageDTO<>();
			List<HomeworkDTO> homeworkDTOs = new ArrayList<>();

			// 정렬 기준
			List<Sort.Order> sorts = new ArrayList<>();
			sorts.add(Sort.Order.desc("created"));
			Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(sorts));

			// Page 조회
			Page<Homework> pageOfHomework = null;
			
			if(status != null) {
				pageOfHomework = homeworkRepository.findAllByUsernameAndStatus(username, status, pageable);
			} else {
				pageOfHomework = homeworkRepository.findAllByUsername(username, pageable);
			}
			
			// Page 전체 정보 저장
			pageDTO.setCurrentPage(pageOfHomework.getNumber() + 1);
			pageDTO.setTotalPage(pageOfHomework.getTotalPages());
			
			for(Homework homework : pageOfHomework.getContent()) {
				
				// 학생에 대한 숙제 status 불러오기
				List<HomeworkOfStudent> homeworkOfStudents = homeworkOfStudentRepository.findAllByHomeworkIdAndUsername(homework.getId(), username);
				
				Status status_;
				
				if (homeworkOfStudents.size() == 0) {
					status_ = Status.TODO;
				} else {
					status_ = homeworkOfStudents.get(0).getStatus();
				}
				
				homeworkDTOs.add(HomeworkDTO.builder()
										    .id(homework.getId())
										    .title(homework.getTitle())
										    .teacherDTO(TeacherDTO.builder()
										 			   .id(homework.getTeacher().getId())
										 			   .nickname(homework.getTeacher().getNickname())
										 		       .build())
										    .classroomDTO(ClassroomDTO.builder()
									    			 .id(homework.getClassroom().getId())
									    			 .classname(homework.getClassroom().getClassname())
									    			 .build())
										    //.teacherId(homework.getTeacher().getId())
										    .created(homework.getCreated())
										    .updated(homework.getUpdated())
										    .deadline(homework.getDeadline())
										    .status(status_)
										    .build());
			}
			
			pageDTO.setResults(homeworkDTOs);
			
			return pageDTO;
			
		} catch (Exception e) {
			log.info("Failed e : " + e.getMessage());
		}
		return null;
	}
	

	@Override
	public HomeworkDTO getHomework(Long classId, Long homeworkId) {

		try {
			Homework homework = homeworkRepository.findById(homeworkId).get();
			
			HomeworkDTO homeworkDTO = HomeworkDTO.builder()
											     .id(homework.getId())
												 .title(homework.getTitle())
												 .teacherDTO(TeacherDTO.builder()
														 			   .id(homework.getTeacher().getId())
														 			   .nickname(homework.getTeacher().getNickname())
														 		       .build())
												 .classroomDTO(ClassroomDTO.builder()
										    			 .id(homework.getClassroom().getId())
										    			 .classname(homework.getClassroom().getClassname())
										    			 .build())
												 //.teacherId(homework.getTeacher().getId())
												 .created(homework.getCreated())
												 .updated(homework.getUpdated())
												 .deadline(homework.getDeadline())
												 .description(homework.getDescription())
												 .build();
			return homeworkDTO;
			
		} catch (Exception e) {
			log.info("Failed e : " + e.getMessage());
		}
		return null;
	}
	
	@Override
	public HomeworkDTO getHomework(Long classId, Long homeworkId, String username) {

		try {
			Homework homework = homeworkRepository.findById(homeworkId).get();
			
			// 학생에 대한 숙제 status 불러오기
			List<HomeworkOfStudent> homeworkOfStudents = homeworkOfStudentRepository.findAllByHomeworkIdAndUsername(homework.getId(), username);
			
			Status status;
			
			if (homeworkOfStudents.size() == 0) {
				status = Status.TODO;
			} else {
				status = homeworkOfStudents.get(0).getStatus();
			}
			
			HomeworkDTO homeworkDTO = HomeworkDTO.builder()
											     .id(homework.getId())
												 .title(homework.getTitle())
												 .teacherDTO(TeacherDTO.builder()
														 			   .id(homework.getTeacher().getId())
														 			   .nickname(homework.getTeacher().getNickname())
														 		       .build())
												 .classroomDTO(ClassroomDTO.builder()
										    			 .id(homework.getClassroom().getId())
										    			 .classname(homework.getClassroom().getClassname())
										    			 .build())
												 //.teacherId(homework.getTeacher().getId())
												 .created(homework.getCreated())
												 .updated(homework.getUpdated())
												 .deadline(homework.getDeadline())
												 .description(homework.getDescription())
												 .status(status)
												 .build();
			return homeworkDTO;
			
		} catch (Exception e) {
			log.info("Failed e : " + e.getMessage());
		}
		return null;
	}


	@Override
	public HomeworkDTO createHomework(HomeworkDTO homeworkDTO) {
		
		try {
			// classroom 불러오기
			Optional<Classroom> classroom_optional = classroomRepository.findById(homeworkDTO.getClassroomId());
			if(classroom_optional.isEmpty())
				throw new CustomException(ClassErrorCode.CLASSROOM_NOT_FOUND);
			Classroom classroom = classroom_optional.get();
			
			// teacher 불러오기
			Optional<Teacher> teacher_optional = teacherRepository.findById(homeworkDTO.getTeacherId());
			if(teacher_optional.isEmpty())
				throw new CustomException(UserErrorCode.USER_NOT_FOUND);
			Teacher teacher = teacher_optional.get();
			
			Timestamp now = new Timestamp(System.currentTimeMillis());
			
			Homework homework = Homework.builder()
										.classroom(classroom)
										.teacher(teacher)
										.created(now)
										.updated(now)
										.deadline(homeworkDTO.getDeadline())
										.title(homeworkDTO.getTitle())
										.description(homeworkDTO.getDescription())
										.build();
			
			homeworkRepository.save(homework);
			
			HomeworkDTO homeworkDTO_ = HomeworkDTO.builder()
												 .id(homework.getId())
												 .classroomDTO(ClassroomDTO.builder()
														 				   .id(homework.getClassroom().getId())
														 				   .classname(homework.getClassroom().getClassname())
														 				   .build())
												 //.classroomId(homework.getClassroom().getId())
												 .teacherDTO(TeacherDTO.builder()
											 			   			   .id(homework.getTeacher().getId())
											 			   			   .nickname(homework.getTeacher().getNickname())
											 			   			   .build())
												 //.teacherId(homework.getTeacher().getId())
												 .created(homework.getCreated())
												 .updated(homework.getUpdated())
												 .deadline(homework.getDeadline())
												 .title(homework.getTitle())
												 .description(homework.getDescription())
												 .build();
											
			return homeworkDTO_;
			
		} catch (Exception e) {
			log.info("Failed e : " + e.getMessage());
			
			throw e;
		}
	}

	@Override
	public HomeworkDTO updateHomework(HomeworkDTO homeworkDTO) {

		try {
			// searchedHomework 불러오기
			Optional<Homework> searchedHomework_optional = homeworkRepository.findById(homeworkDTO.getId());
			if(searchedHomework_optional.isEmpty())
				throw new CustomException(HomeworkErrorCode.HOMEWORK_NOT_FOUND);	
			Homework searchedHomework = searchedHomework_optional.get();
			
			if(!ObjectUtils.isEmpty(homeworkDTO.getTitle()))
				searchedHomework.setTitle(homeworkDTO.getTitle());
			if(!ObjectUtils.isEmpty(homeworkDTO.getDescription()))
				searchedHomework.setDescription(homeworkDTO.getDescription());
			if(!ObjectUtils.isEmpty(homeworkDTO.getDeadline()))
				searchedHomework.setDeadline(homeworkDTO.getDeadline());
			
			// 수정 시간 updated 에 입력
			searchedHomework.setUpdated(new Timestamp(System.currentTimeMillis()));
			
			homeworkRepository.save(searchedHomework);
			
			return homeworkDTO;
			
		} catch (Exception e) {
			log.info("Failed e : " + e.getMessage());
			
			throw e;
		}
	}

	@Override
	public boolean deleteHomework(Long classId, Long homeworkId) {

		try {
			Homework searchedHomework = homeworkRepository.findById(homeworkId).get();
			
			homeworkRepository.delete(searchedHomework);
			
			return true;
		} catch (Exception e) {
			log.info("Failed e : " + e.getMessage());
		}
		return false;
	}

}
