package com.nkedu.back.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Setter
@Getter
@Entity
@JsonInclude(Include.NON_NULL)
@Table(name="parent")
public class Parent {
	@Id // primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
	@Column(name="id")
	private Long id;
	
	@Column(name="userId", unique=true)
	private String userId;
	
	@Column(name="userPw")
	private String userPw;
	
	@Column(name="name")
	private String name;
	
	@Column(name="created")
	private Timestamp created;
	
	@Column(name="birth")
	private Date birth;
	
	@Column(name="phone_number")
	private String phone_number;
}
