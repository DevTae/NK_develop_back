package com.nkedu.back.dto;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.nkedu.back.entity.Teacher.Day;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeacherDTO {

    private Long id;

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String nickname;

    private Date birth;

    private String phoneNumber;

    private LocalDate registrationDate;

    private Set<Day> workingDays;

    @JsonProperty(value="Classroom")
    private List<TeacherOfClassroomDTO> teacherOfClassroomDTO;
}