package com.gd.signup.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@MappedSuperclass
@SequenceGenerator(
        name = "MEMBERS_SEQ_GENERATOR",
        sequenceName = "MEMBERS_SEQ",
        initialValue = 1, allocationSize = 50
)

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseMember {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBERS_SEQ_GENERATOR")
    @Column(name = "member_id")
    protected Long id;

}