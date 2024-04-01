package com.nkedu.back.dto;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nkedu.back.entity.ParentOfStudent.Relationship;
import com.nkedu.back.entity.Student;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
@JsonInclude(Include.NON_NULL)
public class ParentDTO {
	
	private Long id;
	
	private String username;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // 쓰기 전용
	private String password;
	
	private String nickname;
	
	private Date birth;
	
	private String phoneNumber;
	
	// 부모님 계정에 속한 학생 리스트
	@JsonProperty("students")
	private List<StudentDTO> studentDTOs;
	
	// 1회 다중 요청을 위한 primary key 를 담아둘 list 선언 
	@JsonProperty("studentIds")
	private List<Long> studentIds;
	
	// 학생과의 관계 (미설정 시 NOK 기본값)
	private Relationship relationship;
	
}
