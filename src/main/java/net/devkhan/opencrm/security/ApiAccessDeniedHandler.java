package net.devkhan.opencrm.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.devkhan.opencrm.data.vo.api.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class ApiAccessDeniedHandler implements AccessDeniedHandler {
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
	                   AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		String content = objectMapper.writeValueAsString(
				ApiResponse.bad(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage(), "Access Denied"));
		PrintWriter writer = response.getWriter();
		writer.print(content);
	}
}
