package com.nkedu.back.serviceImpl;

import com.nkedu.back.api.ClassroomService;
import com.nkedu.back.dto.*;
import com.nkedu.back.entity.*;


import com.nkedu.back.repository.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
     * @author beom-i
     */

    @Override
    public boolean createClassroom(ClassroomDTO classroomDTO) {
        try{
            if (!ObjectUtils.isEmpty(classroomRepository.findOneById(classroomDTO.getId()))) {
                throw new RuntimeException("이미 등록된 수업입니다.");
            }
            Classroom classroom = (Classroom) Classroom.builder()
                    .classname(classroomDTO.getClassname())
                    .build();
            classroomRepository.save(classroom);
            return true;
        } catch(Exception e) {
            log.error("Failed: " + e.getMessage(),e);
        }
        return false;
    }

    @Override
    public boolean deleteClassroomById(Long id) {
        try{
            classroomRepository.deleteById(id);
            return true;
        }catch (Exception e){
            log.info("Failed: " + e.getMessage(),e);
        }
        return false;
    }

    @Override
    public boolean updateClassroom(Long id, ClassroomDTO classroomDTO) {
        try {
            Classroom searchedClassroom = classroomRepository.findOneById(id).get();

            if(ObjectUtils.isEmpty(searchedClassroom))
                return false;

            if(!ObjectUtils.isEmpty(classroomDTO.getClassname()))
                searchedClassroom.setClassname(classroomDTO.getClassname());

            classroomRepository.save(searchedClassroom);

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

            return classroomDTO;
        } catch(Exception e){
            log.info("Failed e : " + e.getMessage());
        }
        return null;
    }

    @Override
    public StudentOfClassroomDTO createStudentOfClassroom(StudentOfClassroomDTO studentOfClassroomDTO) {
        try {
            Long class_id = studentOfClassroomDTO.getClassroomDTO().getId();
            Long student_id = studentOfClassroomDTO.getStudentDTO().getId();

            Optional<StudentOfClassroom> validateStudentOfClassroom = studentOfClassroomRepository.findOneByClassroomIdAndStudentId(class_id, student_id);

            // 중복 등록 방지를 위한 조건문
            if (!ObjectUtils.isEmpty(validateStudentOfClassroom)) {
                log.info("이미 존재하는 StudentOfClassroom 입니다. ");
                return null;
            }

            StudentOfClassroom studentOfClassroom = StudentOfClassroom.builder()
                    .classroom(classroomRepository.findOneById(class_id).get())
                    .student(studentRepository.findOneById(student_id).get())
                    .build();

            studentOfClassroomRepository.save(studentOfClassroom);

            return studentOfClassroomDTO;

        } catch (Exception e) {
            log.info("[Failed] e : " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<StudentOfClassroomDTO> getStudentOfClassroomsByClassroomId(Long classroom_id) {
        try{
            List<StudentOfClassroom> studentOfClassrooms = studentOfClassroomRepository.findAllByClassroomId(classroom_id);

            List<StudentOfClassroomDTO> studentOfClassroomDTOs = new ArrayList<>();

            for(StudentOfClassroom studentOfClassroom : studentOfClassrooms){

                StudentOfClassroomDTO studentOfClassroomDTO = StudentOfClassroomDTO.builder()
                        .studentDTO(StudentDTO.builder().id(studentOfClassroom.getStudent().getId()).build())
                        .classroomDTO(ClassroomDTO.builder().id(studentOfClassroom.getClassroom().getId()).build())
                        .build();

                studentOfClassroomDTOs.add(studentOfClassroomDTO);
            }
            return studentOfClassroomDTOs;
        } catch (Exception e){
            log.info("Failed e : " + e.getMessage());
        } return null;
    }


    @Override
    public boolean deleteStudentOfClassroom(StudentOfClassroomDTO studentOfClassroomDTO) {
        try{
            Long classroom_id = studentOfClassroomDTO.getClassroomDTO().getId();
            Long student_id = studentOfClassroomDTO.getStudentDTO().getId();

            studentOfClassroomRepository.delete(studentOfClassroomRepository.findOneByClassroomIdAndStudentId(classroom_id,student_id).get());
            return true;
        } catch (Exception e){
            log.info("Failed e : " + e.getMessage());
        }
        return false;
    }
}
