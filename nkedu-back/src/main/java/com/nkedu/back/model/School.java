package com.nkedu.back.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@DiscriminatorValue("school")
@JsonInclude(Include.NON_NULL)
public class School extends User{
    // @Pattern(regexp = ".*����б�$",message = "***����б� ������ �Է� �����մϴ�.") 
	// -> �߰��ϰ� ������, Valid ���� ������̼��� ������ ���߿� �ذ� �� �߰� ���� 
	@Column(name="schoolName", unique = true)
	private String schoolName;
}
