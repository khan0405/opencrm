package net.devkhan.opencrm.security;

import net.devkhan.opencrm.data.dto.AccountDto;
import net.devkhan.opencrm.data.dto.ApiToken;
import net.devkhan.opencrm.data.vo.api.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtSigninFilter extends AbstractAuthenticationProcessingFilter {

	@Autowired
	private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Autowired
	private MacSigner macSigner;

	protected JwtSigninFilter() {
		super("/api/auth/signIn");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(request);

		AccountDto account = (AccountDto) mappingJackson2HttpMessageConverter.read(AccountDto.class, inputMessage);
		UsernamePasswordAuthenticationToken authRequest =
				new UsernamePasswordAuthenticationToken(account.getUsername(), account.getPassword());

		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));

		return getAuthenticationManager().authenticate(authRequest);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
	                                        Authentication authResult) throws IOException, ServletException {
		Object principal = authResult.getPrincipal();

		String content = mappingJackson2HttpMessageConverter.getObjectMapper().writeValueAsString(principal);
		Jwt jwt = JwtHelper.encode(content, macSigner);
		String token = jwt.getEncoded();
		ApiToken apiToken = new ApiToken();
		apiToken.setApiKey(token);
		ApiResponse apiResponse = ApiResponse.ok(apiToken);

		mappingJackson2HttpMessageConverter.write(apiResponse, MediaType.APPLICATION_JSON, new ServletServerHttpResponse(response));
	}
}
