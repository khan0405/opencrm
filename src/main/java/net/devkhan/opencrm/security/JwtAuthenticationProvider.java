package net.devkhan.opencrm.security;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import net.devkhan.opencrm.data.entity.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider, InitializingBean {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MacSigner macSigner;

	public JwtAuthenticationProvider() {
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public void setMacSigner(MacSigner macSigner) {
		this.macSigner = macSigner;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (macSigner == null) {
			throw new IllegalArgumentException("require macSigner");
		}
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
		try {
			String jwtToken = token.getCredentials();
			Jwt jwt = JwtHelper.decodeAndVerify(jwtToken, macSigner);
			String content = jwt.getClaims();
			User user = objectMapper.readValue(content, User.class);
			log.debug("authenticated user:{}", user);
			Authentication authResult = new JwtAuthenticationToken(user, jwtToken, user.getAuthorities());
			log.debug("authenticated:{}", authResult);
			return authResult;
		} catch (Exception e) {
			log.debug("token Exception", e);
			throw new InvalidTokenException("invalid token!!", e);
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
