package com.nkedu.back.entity;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author devtae
 * 숙제 정보를 담는 Entity 코드입니다.
 */

@Entity
@Table(name="homework")
@Setter
@Getter
@SuperBuilder
@RequiredArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Homework {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="classroom_id", referencedColumnName="id")
	private Classroom classroom;
	
	@ManyToOne
	@JoinColumn(name="teacher_id", referencedColumnName="id")
	private Teacher teacher;
	
	@Column(name="title")
	private String title;
	
	@Column(name="description")
	private String description;
	
	@Column(name="created")
	private Timestamp created;
	
	@Column(name="updated")
	private Timestamp updated;
	
	@Column(name="deadline")
	private Timestamp deadline;
	
	
	@OneToMany(mappedBy="homework", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<HomeworkOfStudent> homeworkOfStudents;
	
	@OneToMany
	private List<FileData> files;
	
}
