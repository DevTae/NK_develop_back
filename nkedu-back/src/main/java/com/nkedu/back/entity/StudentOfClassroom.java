package com.nkedu.back.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name="student_of_class")
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentOfClassroom {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "classroom_id", referencedColumnName = "id")
    private Classroom classroom;

    @Column(name="activated")
    private boolean activated;

//    추후 개발 예정 by 시우님
//    @Column(name="rate", nullable=false)
//    private Long rate;
//
//    @Column(name="content", length=1000, nullable=false)
//    private String content;
}
