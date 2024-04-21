package com.nkedu.back.entity;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author devtae
 * 학생이 올린 숙제 정보를 담는 Entity 코드입니다.
 */

@Entity
@Table(name="homework_of_student")
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HomeworkOfStudent {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="homework_id", referencedColumnName="id")
	private Homework homework;
	
	@ManyToOne
	@JoinColumn(name="student_id", referencedColumnName="id")
	private Student student;
	
	@Enumerated(EnumType.STRING)
	@Column(name="status")
	private Status status;
	
	// 한 숙제에 여러 파일이 들어갈 수 있도록 JoinTable 설정
	@ManyToMany(cascade=CascadeType.ALL) // 파일 정보가 삭제될 때 반영되도록 cascade 진행 (일부만)
	@JoinTable(
			name="homework_of_student_of_file",
			joinColumns= {@JoinColumn(name="homework_of_student_id", referencedColumnName="id")},
			inverseJoinColumns= {@JoinColumn(name="file_id", referencedColumnName="id")})
	private List<FileData> files;
	
	// 선생님 숙제 반려 시, feedback 작성 공간
	@Column(name="feedback", nullable=true)
	private String feedback;
	
	@Column(name="created")
	private Timestamp created;
	
	@Column(name="updated")
	private Timestamp updated;
	
	// 숙제 상태 enum 제공
	public enum Status {
		TODO, // 아무런 진행 상태 없음
		SUBMIT, // 제출 완료 (선생님 검토 필요)
		COMPLETE, // 숙제 완료
		REJECT // 반려 상태
	}
}
