package com.gd.signup.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
public record SocialLoginRequest (@NotBlank String provider, @NotBlank String accessToken) {}
