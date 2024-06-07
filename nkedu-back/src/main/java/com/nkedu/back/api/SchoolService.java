package com.nkedu.back.api;

import java.util.List;

import com.nkedu.back.dto.SchoolDTO;

public interface SchoolService {

	public boolean createSchool(SchoolDTO schoolDTO);
	
    public boolean deleteBySchoolName(String schoolName);

    public boolean deletesBySchoolName(SchoolDTO schoolDTO);

//	public boolean updateSchool(String schoolName, SchoolDTO schoolDTO);
	
    public List<SchoolDTO> getSchools();
}
