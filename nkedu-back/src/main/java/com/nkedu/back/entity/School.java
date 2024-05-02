package com.nkedu.back.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@JsonInclude(Include.NON_NULL)
public class School {

    // @Pattern(regexp = ".*고등학교$",message = "***고등학교 형태의 입력 가능합니다.")
	@Id
	@Column(name="school_name",unique = true)
	private String schoolName;

}
