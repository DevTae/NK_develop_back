package com.nkedu.back.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nkedu.back.dto.TeacherOfClassroomDTO;
import lombok.*;

import java.sql.Date;
import java.util.List;

/**
 * 해당 DTO는 Teacher 조회시 Teacehr가 속해있는 Classroom 정보도 보여주기 위해 특별히 만들어진 DTO 입니다.
 * */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherWithClassroomDTO {

    private Long id;

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String nickname;

    private Date birth;

    private String phoneNumber;

    @JsonProperty(value="Classroom")
    private List<TeacherOfClassroomDTO> teacherOfClassroomDTO;

}
