package com.nkedu.back.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchoolDTO {

	private String schoolName;

	// 다중 삭제를 위한 학교 이름 리스트
	@JsonProperty("schoolNames")
	private List<String> schoolNames;

}
