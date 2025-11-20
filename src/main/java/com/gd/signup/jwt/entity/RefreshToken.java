package com.gd.signup.jwt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter @Setter //Todo 이거 setter 나중에 수정해야 함
public class RefreshToken {
	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String token;

	@Column(nullable = false, unique = true)
	private Long memberId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date expiresAt;
}
