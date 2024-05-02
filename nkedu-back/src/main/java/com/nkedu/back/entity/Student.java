package com.nkedu.back.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@SuperBuilder
@RequiredArgsConstructor
@DiscriminatorValue("student")
@JsonInclude(Include.NON_NULL)
public class Student extends User {

	@ManyToOne
	@JoinColumn(name = "school_name", referencedColumnName = "school_name") // 학교 테이블의 schoolName 컬럼과 조인합니다.
	private School school; // 학생이 속한 학교를 나타내는 필드

	@Column(name="grade")
	private Long grade;

	@Column(name="registrationDate", nullable=false)
	private LocalDate registrationDate;


}

