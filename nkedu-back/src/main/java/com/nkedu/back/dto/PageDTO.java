package com.nkedu.back.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

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
public class PageDTO<T> {
	// 페이지 정보
	Integer totalPage;
	
	Integer currentPage;
	
	// Json 페이지 형식 반환
	List<T> results;
	
}
