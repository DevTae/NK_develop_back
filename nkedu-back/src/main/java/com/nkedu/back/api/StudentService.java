package com.nkedu.back.api;

import java.util.List;

import com.nkedu.back.dto.ClassroomDTO;
import com.nkedu.back.dto.PageDTO;
import com.nkedu.back.dto.StudentDTO;


public interface StudentService {
	
	public boolean createStudent(StudentDTO studentDTO);
	
    public boolean deleteByUsername(String username);

	public boolean updateStudent(String username, StudentDTO studentDTO);
	
    public List<StudentDTO> getStudents();

    public PageDTO<StudentDTO> getStudentsByKeyword(Integer page, String keyword);

    public StudentDTO findByUsername(String username);


}
