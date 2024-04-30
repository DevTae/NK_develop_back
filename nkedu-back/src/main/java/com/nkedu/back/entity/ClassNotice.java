package com.nkedu.back.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name="class_notice")
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassNotice {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @Column(name="title", length=100, nullable=false)
    private String title;

    @Column(name="content", length=1000, nullable=false)
    private String content;


    @JsonIgnore
    @Column(name="created", nullable=false)
    private Timestamp created;

    @Column(name="updated", nullable=false)
    private Timestamp updated;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass= ClassNoticeType.class)
    @CollectionTable(name="class_notice_types", joinColumns=@JoinColumn(name="notice_id"))
    @Column(name = "type")
    private Set<ClassNoticeType> classNoticeType;


    /**
     * 수업 공지 선택지 - STUDENT PARENT
     * */
    public enum ClassNoticeType {
        STUDENT,
        PARENT
    }
}
