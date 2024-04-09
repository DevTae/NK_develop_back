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

    // 수업 요일
    private Set<Day> days;

    // Teaching 선생님의 정보
    @JsonProperty(value="TeachingTeacher")
    private TeacherDTO teachingTeacher;

    // Assistant 선생님의 정보
    @JsonProperty(value="AssistantTeacher")
    private List<TeacherDTO> assistantTeachers;

    // 반에 속해져 있는 학생들의 주키
    @JsonProperty("studentIds")
    private List<Long> studentIds;
}
