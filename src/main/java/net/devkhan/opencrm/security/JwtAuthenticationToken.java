package net.devkhan.opencrm.security;

import net.devkhan.opencrm.data.entity.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	private User user;
	private String jwtToken;

	public JwtAuthenticationToken(String jwtToken) {
		super(null);
		this.jwtToken = jwtToken;
		setAuthenticated(false);
	}

	public JwtAuthenticationToken(User user, String jwtToken, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.user = user;
		this.jwtToken = jwtToken;
		setAuthenticated(true);
	}

	@Override
	public String getCredentials() {
		return jwtToken;
	}

	@Override
	public User getPrincipal() {
		return user;
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		this.jwtToken = null;
	}
}
