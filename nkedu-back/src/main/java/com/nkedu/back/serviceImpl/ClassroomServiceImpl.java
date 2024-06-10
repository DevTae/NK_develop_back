package com.nkedu.back.serviceImpl;

import com.nkedu.back.api.ClassroomService;
import com.nkedu.back.dto.*;
import com.nkedu.back.entity.*;


import com.nkedu.back.exception.errorCode.ClassErrorCode;
import com.nkedu.back.exception.exception.CustomException;
import com.nkedu.back.repository.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final StudentOfClassroomRepository studentOfClassroomRepository;
    private final TeacherOfClassroomRepository teacherOfClassroomRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    /**
     * Classroom 엔티티의 CRUD 관련 API 구현 코드입니다.
     *
     * 예범) 과정중에 하나라도 오류가 나온다면 롤백되어야 하기 때문에, Transactional 어노테이션을 추가하였습니다.
     * @author beom-i
     */

    @Override
    @Transactional
    public boolean createClassroom(ClassroomDTO classroomDTO) {

        Classroom classroom = null;
        boolean isSaved = false;

        try{
            if (!ObjectUtils.isEmpty(classroomRepository.findOneClassroomById(classroomDTO.getId()))) {
                throw new CustomException(ClassErrorCode.DUPLICATE_CLASSROOM);
            }

            /**
             * 1. 요청받은 ClassroomDTO를 통해 새롭게 만든다. (Id는 새롭게 생성이므로 추가하지 않아도 내부적으로 생성됨)
             * */
                classroom = (Classroom) Classroom.builder()
                    .classname(classroomDTO.getClassname())
                    .days(EnumSet.copyOf(classroomDTO.getDays()))
                    .activated(true)
                    .build();

            classroomRepository.save(classroom);
            /**
             * 2. 요청받은 ClassroomDTO 내부에 TeachingTeacher를 연결 (TeachingTeacher의 type = true)
             * */

//            // 숙제 및 학생에 대한 확인
//             homework = homeworkRepository.findById(homeworkId).get();
//            if(ObjectUtils.isEmpty(homework)) {
//                throw new Exception("homework doesn't found");
//            }
//            Student student = studentRepository.findById(studentId).get();
//            if(ObjectUtils.isEmpty(student)) {
//                throw new Exception("student doesn't found");
//            }

            Long classroom_id = classroom.getId();

            // 선생님이 empty라면 오류 발생
            if (ObjectUtils.isEmpty(teacherRepository.findOneById(classroomDTO.getTeachingTeacher().getId()))) {
                classroomRepository.delete(classroom);
                throw new CustomException(ClassErrorCode.TEACHING_TEACHER_NOT_FOUND);
            }

            Long teaching_teacher_id = classroomDTO.getTeachingTeacher().getId();

            TeacherOfClassroom teachingTeacherOfClassroom = TeacherOfClassroom.builder()
                    .classroom(classroomRepository.findOneClassroomById(classroom_id).get())
                    .teacher(teacherRepository.findOneById(teaching_teacher_id).get())
                    .type(true)
                    .build();

            teacherOfClassroomRepository.save(teachingTeacherOfClassroom);
            System.out.println("!");
            isSaved = true;

            /**
             * 3. 요청받은 ClassroomDTO 내부에 AssistantTeacher 연결 (AssistantTeacher의 type =  false)
             * */

            List<TeacherDTO> assistantTeachers = classroomDTO.getAssistantTeachers();

            for(TeacherDTO assistantTeacherDTO : assistantTeachers){

                if (ObjectUtils.isEmpty(teacherRepository.findOneById(assistantTeacherDTO.getId()))) {
                    classroomRepository.delete(classroom);
                    throw new CustomException(ClassErrorCode.ASSISTANT_TEACHER_NOT_FOUND);
                }

                Long assistant_teacher_id = assistantTeacherDTO.getId();

                TeacherOfClassroom assistantTeacherOfClassroom = TeacherOfClassroom.builder()
                        .classroom(classroomRepository.findOneClassroomById(classroom_id).get())
                        .teacher(teacherRepository.findOneById(assistant_teacher_id).get())
                        .type(false)
                        .build();
                teacherOfClassroomRepository.save(assistantTeacherOfClassroom);
            }
            return true;
        } catch (Exception e) {
            if(isSaved && classroom != null)
                classroomRepository.delete(classroom);
            throw e;
        }
    }

    @Override
    public boolean deleteClassroomById(Long classroom_id) {
        try{

            Classroom classroom = classroomRepository.findOneClassroomById(classroom_id).get();

            classroom.setActivated(false);

            classroomRepository.save(classroom);

            return true;
        }catch (Exception e){
            log.info("Failed: " + e.getMessage(),e);
        }
        return false;
    }

    /**
     * 예범Think)
     * 2-3번 과정을 각 매핑테이블을 수정하려고 했다. 하지만, 제약조건이 너무 많았다. AssistantTeacher 같은 경우
     * 처음엔 2명이었고 나중엔 3명이어서 개수가 변경될 수 있으며, 도저히 있는 매핑을 그대로 수정할 수 없다고 판단했다.
     * 그래서 2-3번은 통일하게 삭제 후 생성하는 것으로 개발 방법을 변경했다.
     *
     * @author beom-i
     * */
    @Override
    @Transactional
    public boolean updateClassroom(Long classroom_id, ClassroomDTO classroomDTO) {
        try {

            /**
             * 1. Classroom 테이블을 수정하는 과정
             * */
            Optional<Classroom> optionalClassroom = classroomRepository.findOneClassroomById(classroom_id);

            if (!optionalClassroom.isPresent()) {
                throw new CustomException(ClassErrorCode.CLASSROOM_NOT_FOUND);
            }

            if (ObjectUtils.isEmpty(teacherRepository.findOneById(classroomDTO.getTeachingTeacher().getId()))) {
                throw new CustomException(ClassErrorCode.TEACHING_TEACHER_NOT_FOUND);
            }

            for(TeacherDTO assistantTeacherDTO : classroomDTO.getAssistantTeachers()) {

                if (ObjectUtils.isEmpty(teacherRepository.findOneById(assistantTeacherDTO.getId()))) {
                    throw new CustomException(ClassErrorCode.ASSISTANT_TEACHER_NOT_FOUND);
                }
            }


            Classroom searchedClassroom = optionalClassroom.get();

            if(!ObjectUtils.isEmpty(classroomDTO.getClassname()))
                searchedClassroom.setClassname(classroomDTO.getClassname());
            if(!ObjectUtils.isEmpty(classroomDTO.getDays()))
                searchedClassroom.setDays(classroomDTO.getDays());

            classroomRepository.save(searchedClassroom);

            /** clasroom_id에 해당하는 매핑 테이블을 모조리 삭제 */
            List<TeacherOfClassroom> teacherOfClassrooms = teacherOfClassroomRepository.findAllByClassroomId(classroom_id);
            for(TeacherOfClassroom teacherOfClassroom : teacherOfClassrooms){
                teacherOfClassroomRepository.deleteById(teacherOfClassroom.getId());
            }

            /**
             * 2. TeachingTeacherOfClassroom 테이블을 생성하는 과정
             * */

            Long teaching_teacher_id = classroomDTO.getTeachingTeacher().getId();

            // classroomId 한개만이어야함
            TeacherOfClassroom teachingTeacherOfClassroom = TeacherOfClassroom.builder()
                    .classroom(classroomRepository.findOneClassroomById(classroom_id).get())
                    .teacher(teacherRepository.findOneById(teaching_teacher_id).get())
                    .type(true)
                    .build();

            teacherOfClassroomRepository.save(teachingTeacherOfClassroom);

            /**
             * 3. AssistantTeacherOfClassroom 테이블 생성하는 과정
             * */

            List<TeacherDTO> assistantTeachers = classroomDTO.getAssistantTeachers();

            for(TeacherDTO assistantTeacherDTO : assistantTeachers){

                Long assistant_teacher_id = assistantTeacherDTO.getId();

                TeacherOfClassroom assistantTeacherOfClassroom = TeacherOfClassroom.builder()
                        .classroom(classroomRepository.findOneClassroomById(classroom_id).get())
                        .teacher(teacherRepository.findOneById(assistant_teacher_id).get())
                        .type(false)
                        .build();
                teacherOfClassroomRepository.save(assistantTeacherOfClassroom);
            }
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<ClassroomDTO> getClassrooms() {
        try {
            List<ClassroomDTO> classroomDTOs = new ArrayList<>();

            List<Classroom> classrooms = classroomRepository.findAllClassroom();

            for(Classroom classroom : classrooms) {
                ClassroomDTO classroomDTO = new ClassroomDTO();
                classroomDTO.setId(classroom.getId());
                classroomDTO.setClassname(classroom.getClassname());
                classroomDTO.setDays(EnumSet.copyOf(classroom.getDays()));

                // 1. classroom_id에 해당하는 관계테이블을 가져옴
                List<TeacherOfClassroom> teacherofclassrooms = teacherOfClassroomRepository.findAllByClassroomId(classroom.getId());

                // 2. AssistantTeacher는 리스트로 저장되어 있으므로 추가할 리스트 생성
                List<TeacherDTO> assistantTeacherDTOs = new ArrayList<>();

                for(TeacherOfClassroom teacherofclassroom : teacherofclassrooms){
                    Long teacher_id = teacherofclassroom.getTeacher().getId();
                    // true이면 TeachingTeacher에 추가
                    if(teacherofclassroom.isType()){
                        TeacherDTO teachingTeacherDTO = TeacherDTO.builder()
                                .id(teacher_id)
                                .nickname(teacherRepository.findOneById(teacher_id).get().getNickname())
                                .build();

                        classroomDTO.setTeachingTeacher(teachingTeacherDTO);
                    }
                    // false이면 assisTeachers에 추가하여 한번에 저장
                    else if(!teacherofclassroom.isType()){
                        TeacherDTO assistantTeacherDTO = TeacherDTO.builder()
                                .id(teacher_id)
                                .nickname(teacherRepository.findOneById(teacher_id).get().getNickname())
                                .build();

                        assistantTeacherDTOs.add(assistantTeacherDTO);
                    }
                }
                classroomDTO.setAssistantTeachers(assistantTeacherDTOs);

                classroomDTOs.add(classroomDTO);
            }
            return classroomDTOs;
        } catch(Exception e) {
            log.info("[Failed] e : " + e.getMessage());
        }

        return null;
    }
    
    @Override
    public PageDTO<ClassroomDTO> getClassroomsByKeyword(Integer page,String keyword) {
        try {

            PageDTO<ClassroomDTO> pageDTO = new PageDTO<>();
            List<ClassroomDTO> classroomDTOs = new ArrayList<>();

            // 정렬 기준
            List<Sort.Order> sorts = new ArrayList<>();
            sorts.add(Sort.Order.asc("classname"));
            Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

            // Page 조회
            Page<Classroom> pageOfClassroom = classroomRepository.findAllByClassname(pageable,keyword);

            pageDTO.setCurrentPage(pageOfClassroom.getNumber() + 1);
            pageDTO.setTotalPage(pageOfClassroom.getTotalPages());

            for(Classroom classroom : pageOfClassroom.getContent()) {
                ClassroomDTO classroomDTO = new ClassroomDTO();
                classroomDTO.setId(classroom.getId());
                classroomDTO.setClassname(classroom.getClassname());
                classroomDTO.setDays(EnumSet.copyOf(classroom.getDays()));

                // 1. classroom_id에 해당하는 관계테이블을 가져옴
                List<TeacherOfClassroom> teacherofclassrooms = teacherOfClassroomRepository.findAllByClassroomId(classroom.getId());

                // 2. AssistantTeacher는 리스트로 저장되어 있으므로 추가할 리스트 생성
                List<TeacherDTO> assistantTeacherDTOs = new ArrayList<>();

                for(TeacherOfClassroom teacherofclassroom : teacherofclassrooms){
                    Long teacher_id = teacherofclassroom.getTeacher().getId();
                    // true이면 TeachingTeacher에 추가
                    if(teacherofclassroom.isType()){
                        TeacherDTO teachingTeacherDTO = TeacherDTO.builder()
                                .id(teacher_id)
                                .nickname(teacherRepository.findOneById(teacher_id).get().getNickname())
                                .build();

                        classroomDTO.setTeachingTeacher(teachingTeacherDTO);
                    }
                    // false이면 assisTeachers에 추가하여 한번에 저장
                    else if(!teacherofclassroom.isType()){
                        TeacherDTO assistantTeacherDTO = TeacherDTO.builder()
                                .id(teacher_id)
                                .nickname(teacherRepository.findOneById(teacher_id).get().getNickname())
                                .build();

                        assistantTeacherDTOs.add(assistantTeacherDTO);
                    }
                }
                classroomDTO.setAssistantTeachers(assistantTeacherDTOs);

                classroomDTOs.add(classroomDTO);
            }
            
            pageDTO.setResults(classroomDTOs);
            
            return pageDTO;
            
        } catch(Exception e) {
            log.info("[Failed] e : " + e.getMessage());
        }

        return null;
    }




    @Override
    public ClassroomDTO getClassroomById(Long id) {
        try{
            Classroom classroom = classroomRepository.findOneClassroomById(id).get();

            ClassroomDTO classroomDTO = new ClassroomDTO();
            classroomDTO.setId(classroom.getId());
            classroomDTO.setClassname(classroom.getClassname());
            classroomDTO.setDays(EnumSet.copyOf(classroom.getDays()));

            // 1. classroom_id에 해당하는 관계테이블을 가져옴
            List<TeacherOfClassroom> teacherofclassrooms = teacherOfClassroomRepository.findAllByClassroomId(classroom.getId());
            // 2. AssistantTeacher는 리스트로 저장되어 있으므로 추가할 리스트 생성
            List<TeacherDTO> assistantTeacherDTOs = new ArrayList<>();
            for(TeacherOfClassroom teacherofclassroom : teacherofclassrooms){
                Long teacher_id = teacherofclassroom.getTeacher().getId();

                // true이면 TeachingTeacher에 추가
                if(teacherofclassroom.isType()){
                    TeacherDTO teachingTeacherDTO = TeacherDTO.builder()
                            .id(teacher_id)
                            .nickname(teacherRepository.findOneById(teacher_id).get().getNickname())
                            .build();

                    classroomDTO.setTeachingTeacher(teachingTeacherDTO);
                }
                // false이면 assisTeachers에 추가하여 한번에 저장
                else if(!teacherofclassroom.isType()){
                    TeacherDTO assistantTeacherDTO = TeacherDTO.builder()
                            .id(teacher_id)
                            .nickname(teacherRepository.findOneById(teacher_id).get().getNickname())
                            .build();

                    assistantTeacherDTOs.add(assistantTeacherDTO);
                }
            }
            classroomDTO.setAssistantTeachers(assistantTeacherDTOs);

            return classroomDTO;

        } catch(Exception e){
            log.info("Failed e : " + e.getMessage());
        }
        return null;
    }

    /**
     * 수업-학생 관련 API 입니다.
     *
     * @author beom-i
     * */
    @Transactional
    @Override
    public boolean createStudentOfClassroom(Long classroom_id, ClassroomDTO classroomDTO) {
        try {

            Classroom validateClassroom = classroomRepository.findOneClassroomById(classroom_id).get();
            // Classroom의 존재 여부 판단
            if (ObjectUtils.isEmpty(validateClassroom)) {
                throw new CustomException(ClassErrorCode.CLASSROOM_NOT_FOUND);
            }

            // studentIds 없으면 Pass
            if(!ObjectUtils.isEmpty(classroomDTO.getStudentIds())){
                for(Long student_id : classroomDTO.getStudentIds()){

                    // 만약 넣은 student id가 없으면 Pass
                    // 2024.05.22 에러 메시지 할 때, 이거 그냥 사실 애초에 없으면 get에 뜨지도 않을 거임.. 그래서 에러 패스
                    Optional<Student> optionalStudent = studentRepository.findOneById(student_id);
                    if (!optionalStudent.isPresent()) continue;

                    Student student = optionalStudent.get();
                    Optional<StudentOfClassroom> optionalStudentOfClassroom = studentOfClassroomRepository.findOneByClassroomIdAndStudentId(classroom_id, student_id);

                    // 1. 매핑 테이블이 아예 없는 경우
                    if (!optionalStudentOfClassroom.isPresent()) {
                        studentOfClassroomRepository.save(StudentOfClassroom.builder()
                                .classroom(validateClassroom)
                                .student(student)
                                .activated(true)
                                .build());
                    }
                    // 2. 매핑 테이블이 있는데 activated = false인 경우 -> activated = true로 바꿔줌
                    else{
                        StudentOfClassroom studentOfClassroom = optionalStudentOfClassroom.get();
                        if (!studentOfClassroom.isActivated()) {
                            studentOfClassroom.setActivated(true);
                            studentOfClassroomRepository.save(studentOfClassroom);
                        }
                    }
                    // 3. 매핑 테이블이 있는데 actiaved = true인 경우 -> 아무일도 없음
                }
            }
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<StudentDTO> getStudentOfClassroomsByClassroomId(Long classroom_id) {
        try{
            List<StudentOfClassroom> studentOfClassrooms = studentOfClassroomRepository.findAllByClassroomId(classroom_id);

            List<StudentDTO> studentDTOs = new ArrayList<>();

            for(StudentOfClassroom studentOfClassroom : studentOfClassrooms){

                // activated 가 활성화 되어있는 매핑 테이블만 가져옴
                if(studentOfClassroom.isActivated()) {
                    StudentDTO studentDTO = StudentDTO.builder()
                            .id(studentOfClassroom.getStudent().getId())
                            .nickname(studentOfClassroom.getStudent().getNickname())
                            .birth(studentOfClassroom.getStudent().getBirth())
                            .phoneNumber(studentOfClassroom.getStudent().getPhoneNumber())
                            .schoolName(studentOfClassroom.getStudent().getSchool().getSchoolName())
//                            .grade(studentOfClassroom.getStudent().getGrade())
//                            .registrationDate(studentOfClassroom.getStudent().getRegistrationDate())
                            .build();

                    studentDTOs.add(studentDTO);
                }
            }

            return studentDTOs;

        } catch (Exception e){
            log.info("Failed e : " + e.getMessage());
        } return null;
    }


    @Transactional
    @Override
    public boolean deleteStudentOfClassroom(Long classroom_id, ClassroomDTO classroomDTO) {
        try{

            Optional<Classroom> optionalClassroom = classroomRepository.findOneClassroomById(classroom_id);
            // Classroom의 존재 여부
            if (!optionalClassroom.isPresent()) {
                log.info("존재하지 않는 Classroom 입니다. ");
                return false;
            }

            // studentIds 없으면 Pass
            if(!ObjectUtils.isEmpty(classroomDTO.getStudentIds())){
                for(Long student_id : classroomDTO.getStudentIds()){

                    // 만약 넣은 student id가 없으면 Pass
                    Optional<Student> optionalStudent = studentRepository.findOneById(student_id);
                    if (!optionalStudent.isPresent()) continue;

                    // 활성화 되어있든 안되어있든 일단 가져옴
                    StudentOfClassroom studentOfClassroom = studentOfClassroomRepository.findOneByClassroomIdAndStudentId(classroom_id,student_id).get();

                    // activated = false로 비활성화 처리하고 저장해줌
                    studentOfClassroom.setActivated(false);
                    studentOfClassroomRepository.save(studentOfClassroom);
                }
            }
            return true;

        } catch (Exception e){
            log.info("Failed e : " + e.getMessage());
        }
        return false;
    }
}
