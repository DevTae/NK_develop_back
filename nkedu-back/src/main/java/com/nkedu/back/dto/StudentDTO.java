package com.nkedu.back.dto;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.nkedu.back.entity.School;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentDTO {
	
	private Long id;
	
	private String username;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	private String nickname;
	
	private Date birth;
	
	private String phoneNumber;
	
	private String schoolName;
	
	private Long grade;

	private LocalDate registrationDate;

	// GET /api/student 에서 학생 부모님 id 및 이름까지 가져올 수 있도록 구현 
	@JsonProperty("parents")
	private List<ParentDTO> parentDTOs;
	
}
