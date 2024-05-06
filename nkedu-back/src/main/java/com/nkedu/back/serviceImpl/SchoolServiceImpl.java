package com.nkedu.back.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.nkedu.back.entity.School;
import com.nkedu.back.dto.SchoolDTO;
import com.nkedu.back.repository.SchoolRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.nkedu.back.api.SchoolService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService{

	private final SchoolRepository schoolRepository;

	// 학교 생성
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

	// 등록된 학교 삭제
	public boolean deleteBySchoolname(String schoolName) {
		try{
			schoolRepository.delete(schoolRepository.findBySchoolName(schoolName).get());

			return true;
		} catch (Exception e) {
			log.info("failed e : " + e.getMessage());
		}
		return false;
	}


	// 등록된 모든 학교 리스트 조회
	public List<SchoolDTO> getSchools() {
		try {
			List<SchoolDTO> schoolDTOs = new ArrayList<>();

			List<School> schools = schoolRepository.findAll();

			for(School school : schools) {
				SchoolDTO schoolDTO = new SchoolDTO();
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
