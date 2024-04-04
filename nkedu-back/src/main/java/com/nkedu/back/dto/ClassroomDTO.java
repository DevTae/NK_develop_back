package com.nkedu.back.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nkedu.back.entity.Classroom.Day;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassroomDTO {

    private Long id;

    private String classname;

    @JsonProperty(value="TeachingTeacher")
    private TeacherDTO teachingTeacher;

    @JsonProperty(value="AssistantTeacher")
    private List<TeacherDTO> assistantTeachers;

    private Set<Day> days;
}
