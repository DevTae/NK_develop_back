package com.nkedu.back.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Setter
@Getter
@SuperBuilder
@RequiredArgsConstructor
@DiscriminatorValue("teacher")
@JsonInclude(Include.NON_NULL)
public class Teacher extends User {

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.REMOVE)
    private Set<TeacherOfClassroom> teacherOfClassrooms;
}
