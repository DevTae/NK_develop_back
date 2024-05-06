package com.nkedu.back.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
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

    @Column(name="registrationDate")
    private LocalDate registrationDate;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "working_days", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "workingDays")
    private Set<Day> workingDays;

    public enum Day {
        MONDAY,TUESDAY, WEDNESDAY, THURSDAY,FRIDAY, SATURDAY, SUNDAY
    }

}
