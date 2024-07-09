package com.nkedu.back.dto;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nkedu.back.entity.HomeworkOfStudent.Status;

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
@JsonInclude(value=JsonInclude.Include.NON_NULL)
public class HomeworkOfStudentDTO {
	
	private Long id;
	
	private Long homeworkId;
	
	@JsonProperty("homework")
	private HomeworkDTO homeworkDTO;
	
	private Long studentId;
	
	private String studentName;
	
	@JsonProperty("student")
	private StudentDTO studentDTO;
	
	private Status status;
	
	private String feedback;
	
	private Timestamp created;
	
	private Timestamp updated;
	
	private Double stopwatch;
	
	private List<FileDataDTO> files;
	
	private List<Long> fileIds;
	
}
