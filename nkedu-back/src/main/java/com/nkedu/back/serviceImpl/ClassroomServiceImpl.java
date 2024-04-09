package com.nkedu.back.serviceImpl;

import com.nkedu.back.api.ClassroomService;
import com.nkedu.back.dto.*;
import com.nkedu.back.dto.TeacherDTO;
import com.nkedu.back.entity.*;


import com.nkedu.back.repository.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
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
    public boolean createClassroom(ClassroomDTO classroomDTO) {
        try{
            if (!ObjectUtils.isEmpty(classroomRepository.findOneById(classroomDTO.getId()))) {
                throw new Exception("이미 등록된 수업입니다.");
            }

            /**
             * 1. 요청받은 ClassroomDTO를 통해 새롭게 만든다. (Id는 새롭게 생성이므로 추가하지 않아도 내부적으로 생성됨)
             * */
            Classroom classroom = (Classroom) Classroom.builder()
                    .classname(classroomDTO.getClassname())
                    .days(classroomDTO.getDays())
                    .build();

            classroomRepository.save(classroom);
            System.out.println("classroom id : " + classroom.getId());
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
            Long teaching_teacher_id = classroomDTO.getTeachingTeacher().getId();

//            // 생성하는 과정이므로 중복 여부 확인
//            if (!ObjectUtils.isEmpty(teacherOfClassroomRepository.findOneByClassroomIdAndTeacherId(classroom_id, teaching_teacher_id).get())) {
//                throw new Exception("이미 존재하는 TeacherOfClassroom 입니다.");
//            }


            TeacherOfClassroom teachingTeacherOfClassroom = TeacherOfClassroom.builder()
                    .classroom(classroomRepository.findOneById(classroom_id).get())
                    .teacher(teacherRepository.findOneById(teaching_teacher_id).get())
                    .type(true)
                    .build();

            teacherOfClassroomRepository.save(teachingTeacherOfClassroom);



            /**
             * 3. 요청받은 ClassroomDTO 내부에 AssistantTeacher 연결 (AssistantTeacher의 type =  false)
             * */

            List<TeacherDTO> assistantTeachers = classroomDTO.getAssistantTeachers();

            for(TeacherDTO assistantTeacherDTO : assistantTeachers){

                Long assistant_teacher_id = assistantTeacherDTO.getId();


//                // 생성하는 과정이므로 중복 여부 확인
//                if (!ObjectUtils.isEmpty(teacherOfClassroomRepository.findOneByClassroomIdAndTeacherId(classroom_id, assistant_teacher_id).get())) {
//                    throw new Exception("이미 존재하는 TeacherOfClassroom 입니다.");
//                }

                TeacherOfClassroom assistantTeacherOfClassroom = TeacherOfClassroom.builder()
                        .classroom(classroomRepository.findOneById(classroom_id).get())
                        .teacher(teacherRepository.findOneById(assistant_teacher_id).get())
                        .type(false)
                        .build();
                teacherOfClassroomRepository.save(assistantTeacherOfClassroom);
            }
            System.out.println("3");

            return true;
        } catch(Exception e) {
            log.error("Failed: " + e.getMessage(),e);
        }
        return false;
    }

    // Q. classroom을 삭제하면 그 수업과 선생님이 연결된 매핑 테이블은 자동으로 사라질까? 아니면 get처럼 하나하나 삭제해줘야 할까..?
    @Override
    public boolean deleteClassroomById(Long classroom_id) {
        try{

//            Classroom searchedCalssroom = classroomRepository.findById(classroom_id).get();
//
//            classroomRepository.delete(searchedCalssroom);
                classroomRepository.deleteById(classroom_id);
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
            Classroom searchedClassroom = classroomRepository.findOneById(classroom_id).get();

            if(ObjectUtils.isEmpty(searchedClassroom)) return false;

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
                    .classroom(classroomRepository.findOneById(classroom_id).get())
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
                        .classroom(classroomRepository.findOneById(classroom_id).get())
                        .teacher(teacherRepository.findOneById(assistant_teacher_id).get())
                        .type(false)
                        .build();
                teacherOfClassroomRepository.save(assistantTeacherOfClassroom);
            }


            return true;
        } catch (Exception e) {
            log.info("[Failed] e : " + e.getMessage());
        }

        return false;
    }

    @Override
    public List<ClassroomDTO> getClassrooms() {
        try {
            List<ClassroomDTO> classroomDTOs = new ArrayList<>();

            List<Classroom> classrooms = classroomRepository.findAll();

            for(Classroom classroom : classrooms) {
                ClassroomDTO classroomDTO = new ClassroomDTO();
                classroomDTO.setId(classroom.getId());
                classroomDTO.setClassname(classroom.getClassname());
                classroomDTO.setDays(classroom.getDays());

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
    public ClassroomDTO getClassroomById(Long id) {
        try{
            Classroom classroom = classroomRepository.findOneById(id).get();

            ClassroomDTO classroomDTO = new ClassroomDTO();
            classroomDTO.setId(classroom.getId());
            classroomDTO.setClassname(classroom.getClassname());
            classroomDTO.setDays(classroom.getDays());

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
    @Override
    public boolean createStudentOfClassroom(Long classroom_id, ClassroomDTO classroomDTO) {
        try {

            Classroom validateClassroom = classroomRepository.findOneById(classroom_id).get();
            // Classroom의 존재 여부 판단
            if (ObjectUtils.isEmpty(validateClassroom)) {
                log.info("존재하지 않는 Classroom 입니다. ");
                return false;
            }

            // studentIds 없으면 Pass
            if(!ObjectUtils.isEmpty(classroomDTO.getStudentIds())){
                for(Long student_id : classroomDTO.getStudentIds()){

                    // 만약 넣은 student id가 없으면 Pass (activated로 판단해야함)
                    Student student = studentRepository.findOneById(student_id).get();
                    if(ObjectUtils.isEmpty(student)) continue;

                    StudentOfClassroom studentOfClassroom = StudentOfClassroom.builder()
                            .classroom(classroomRepository.findOneById(classroom_id).get())
                            .student(student)
                            .build();
                    studentOfClassroomRepository.save(studentOfClassroom);
                }
            }
            return true;

        } catch (Exception e) {
            log.info("[Failed] e : " + e.getMessage());
        }

        return false;
    }

    @Override
    public ClassroomDTO getStudentOfClassroomsByClassroomId(Long classroom_id) {
        try{
            List<StudentOfClassroom> studentOfClassrooms = studentOfClassroomRepository.findAllByClassroomId(classroom_id);

            List<Long> studentIds = new ArrayList<>();

            for(StudentOfClassroom studentOfClassroom : studentOfClassrooms){

                studentIds.add(studentOfClassroom.getStudent().getId());
            }
            return ClassroomDTO.builder()
                    .studentIds(studentIds).build();
        } catch (Exception e){
            log.info("Failed e : " + e.getMessage());
        } return null;
    }


    @Override
    public boolean deleteStudentOfClassroom(Long classroom_id, ClassroomDTO classroomDTO) {
        try{

            Classroom validateClassroom = classroomRepository.findOneById(classroom_id).get();
            // Classroom의 존재 여부
            if (ObjectUtils.isEmpty(validateClassroom)) {
                log.info("존재하지 않는 Classroom 입니다. ");
                return false;
            }

            // studentIds 없으면 Pass
            if(!ObjectUtils.isEmpty(classroomDTO.getStudentIds())){
                for(Long student_id : classroomDTO.getStudentIds()){

                    // 만약 넣은 student id가 없으면 Pass (activated로 판단해야함)
                    Student student = studentRepository.findOneById(student_id).get();
                    if(ObjectUtils.isEmpty(student)) continue;

                    studentOfClassroomRepository.delete(studentOfClassroomRepository.findOneByClassroomIdAndStudentId(classroom_id,student_id).get());
                }
            }
            return true;

        } catch (Exception e){
            log.info("Failed e : " + e.getMessage());
        }
        return false;
    }
}
