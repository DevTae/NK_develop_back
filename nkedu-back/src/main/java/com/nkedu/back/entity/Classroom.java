package com.nkedu.back.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Table(name="classroom")
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Classroom {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;

    @Column(name="classname", length=40, nullable=false)
    private String classname;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "classroom_days", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "day")
    private Set<Day> days;

    /**
     * 수업이 진행되는 요일
     * */
    public enum Day {
        MONDAY,THESDAY, WEDNESDAY, THURSDAY,FRIDAY, SATURDAY, SUNDAY
    }
}
