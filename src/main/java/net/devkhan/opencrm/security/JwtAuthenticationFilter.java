package net.devkhan.opencrm.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends GenericFilterBean {

	@Autowired
	private AuthenticationManager authenticationManager;

	protected AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource
			= new WebAuthenticationDetailsSource();

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String header = request.getHeader("Authorization");
		if (header == null || !header.startsWith("SampleSecurity ")) {
			chain.doFilter(request, response);
			return;
		}

		String authToken = header.substring(15);
		log.debug("authToken:{}", authToken);
		JwtAuthenticationToken authRequest = new JwtAuthenticationToken(authToken);
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
		Authentication authResult;
		try {
			authResult = authenticationManager.authenticate(authRequest);

			log.debug("authResult :{}", authResult);
			SecurityContextHolder.getContext().setAuthentication(authResult);
		}
		catch(InternalAuthenticationServiceException failed) {
			logger.error("An internal error occurred while trying to authenticate the user.", failed);
		}
		catch (AuthenticationException failed) {
			// do nothing...
		}

		chain.doFilter(request, response);

		SecurityContextHolder.getContext().setAuthentication(null);
	}
}
