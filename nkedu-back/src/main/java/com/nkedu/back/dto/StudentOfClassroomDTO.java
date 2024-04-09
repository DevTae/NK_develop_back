package com.nkedu.back.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nkedu.back.entity.Classroom;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentOfClassroomDTO {

    @JsonProperty(value="classroom")
    private ClassroomDTO classroomDTO;

    @JsonProperty(value="student")
    private StudentDTO studentDTO;

    // 반에 속해져 있는 학생들의 주키
    @JsonProperty("studentIds")
    private List<Long> studentIds;
}

