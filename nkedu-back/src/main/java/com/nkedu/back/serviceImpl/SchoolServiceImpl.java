package com.nkedu.back.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.nkedu.back.model.School;
import com.nkedu.back.model.SchoolDTO;
import com.nkedu.back.repository.SchoolRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.nkedu.back.api.SchoolService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService{
	
	private final SchoolRepository schoolRepository;
	
	// �б� ���� 
	public boolean createSchool(SchoolDTO schoolDTO) {
		try {
			School school = new School();
			school.setSchoolName(schoolDTO.getSchoolName());
			schoolRepository.save(school);
			return true;
		} catch (Exception e) {
			log.info("Failed e : " + e.getMessage());
		}
		return false;
	}
	
    // ��ϵ� �б� ���� 
    public boolean deleteSchoolById(Long schoolId) {
    	try{
    		schoolRepository.deleteById(schoolId);
    		
    		return true;
    	} catch (Exception e) {
    		log.info("failed e : " + e.getMessage());
    	}
    	return false;
    }
    
    // ��ϵ� �б� ���� 
	public boolean updateSchool(Long schoolId, SchoolDTO schoolDTO) {
		try {
			School searchedSchool = schoolRepository.findById(schoolId).get();
			
			if(ObjectUtils.isEmpty(searchedSchool))
				return false;
			
			// ��û ���� �б� �̸����� ������Ʈ 
			if(!ObjectUtils.isEmpty(schoolDTO.getSchoolName()))
				searchedSchool.setSchoolName(schoolDTO.getSchoolName());
			
			schoolRepository.save(searchedSchool);
			return true;
		} catch (Exception e) {
			log.info("[Failed] e : " + e.getMessage());
		}
		return false;
	}
	
	
    // ��ϵ� ��� �б� ����Ʈ ��ȸ
    public List<SchoolDTO> getAllSchools() {
    	try {
    		List<SchoolDTO> schoolDTOs = new ArrayList<>();
    		
			List<School> schools = schoolRepository.findAll();
			
			for(School school : schools) {
				SchoolDTO schoolDTO = new SchoolDTO();
				
				schoolDTO.setId(school.getId());
				schoolDTO.setSchoolName(school.getSchoolName());
				
				schoolDTOs.add(schoolDTO);
			}
			
			return schoolDTOs;
		} catch(Exception e) {
			log.info("[Failed] e : " + e.getMessage());
		}
		return null;
    }
   
}