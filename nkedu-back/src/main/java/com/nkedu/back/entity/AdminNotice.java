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

@Entity
@Table(name="admin_notice")
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminNotice {


    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;


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
    @Column(name="adminNotice_type",nullable = false)
    private AdminNoticeType adminNoticeType;



    /**
     * 관리자 공지 선택지 - STUDENT PARENT TEACHER STUDENT_PARENT
     *               - STUDENT_PARENT STUDENT_TEACHER PARENT_TEACHER ENTIRE
     * */
    public enum AdminNoticeType {
        STUDENT,
        PARENT,
        TEACHER,
        STUDENT_PARENT,
        STUDENT_TEACHER,
        PARENT_TEACHER,
        ENTIRE
    }
}
