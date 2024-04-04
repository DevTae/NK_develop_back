package com.nkedu.back.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherOfClassroomDTO {

    @JsonProperty(value="classroom")
    private ClassroomDTO classroomDTO;

    @JsonIgnore
    private TeacherDTO teacherDTO;

    private boolean type;

}
