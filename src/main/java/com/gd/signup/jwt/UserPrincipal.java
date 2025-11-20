package com.gd.signup.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record UserPrincipal(Long id, String loginId, Collection<? extends GrantedAuthority> authorities) implements UserDetails {
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities != null ? authorities : List.of();
	}
	@Override public String getPassword() { return ""; }
	@Override public String getUsername() { return loginId; }
	@Override public boolean isAccountNonExpired() { return true; }
	@Override public boolean isAccountNonLocked() { return true; }
	@Override public boolean isCredentialsNonExpired() { return true; }
	@Override public boolean isEnabled() { return true; }
}
