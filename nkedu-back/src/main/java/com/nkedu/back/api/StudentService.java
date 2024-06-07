package com.nkedu.back.api;

import java.util.List;

import com.nkedu.back.dto.ClassroomDTO;
import com.nkedu.back.dto.PageDTO;
import com.nkedu.back.dto.StudentDTO;
import com.nkedu.back.dto.TeacherDTO;


public interface StudentService {
	
	public boolean createStudent(StudentDTO studentDTO);
	
    public boolean deleteByUsername(String username);

    /**
     * 학생 계정 다중 삭제 (비활성화)
     * @param studentDTO
     */
    public boolean deletesById(StudentDTO studentDTO);

	public boolean updateStudent(String username, StudentDTO studentDTO);
	
    public List<StudentDTO> getStudents();

    public PageDTO<StudentDTO> getStudentsByKeyword(Integer page, String keyword);

    public StudentDTO getStudentByUsername(String username);


}
