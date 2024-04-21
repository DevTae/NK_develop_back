package com.nkedu.back.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author devtae
 * 숙제 등에 따른 파일 업로드 상황에 사용할 수 있는 Entity 코드입니다.
 */

@Entity
@Table(name="file_data")
@Setter
@Getter
@SuperBuilder
@RequiredArgsConstructor
@JsonInclude(Include.NON_NULL)
public class FileData {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name", nullable=true)
	private String name;
	
	@Enumerated(EnumType.STRING)
	@Column(name="type")
	private FileType type;
	
	@Column(name="path")
	private String path;

	
	@OneToMany(mappedBy="files", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<HomeworkOfStudent> homeworkOfStudents;
	
	
	/*
	 * 파일 종류에 따라 enum 으로 정의
	 * 추후 필요에 따라 추가할 수 있음. (이미 추가된 속성은 변경하지 않아야 함. 중요!)
	 */
	public enum FileType {
		IMAGE_JPEG("image/png"),
		IMAGE_JPG("image/jpg"),
		IMAGE_GIF("image/gif"),
		IMAGE_PNG("image/png");
		
		private String mimeType; // contentType 반환 시에 적절하게 사용
		
		FileType() {
			this.mimeType = null;
		}
		
		FileType(String mimeType) {
			this.mimeType = mimeType;
		}
		
		public String getMimeType() {
			return this.mimeType;
		}
		
		public static FileType getFileType(String mimeType) {
			switch(mimeType) {
			
			case "image/jpeg":
				return FileType.IMAGE_JPEG;
				
			case "image/jpg":
				return FileType.IMAGE_JPG;
			
			case "image/png":
				return FileType.IMAGE_PNG;
				
			default:
				return null;
			}
		}
	}
	
}
