package com.goodee.corpdesk.schedule.entity;

import com.goodee.corpdesk.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "personal_schedule")
public class PersonalSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String scheduleName;

    @Column(nullable = false)
    private LocalDateTime scheduleDatetime;

    @Column(columnDefinition = "text")
    private String content;

    private String address;

}
