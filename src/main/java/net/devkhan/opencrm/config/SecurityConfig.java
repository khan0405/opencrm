package net.devkhan.opencrm.config;


import net.devkhan.opencrm.security.*;
import net.devkhan.opencrm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, proxyTargetClass = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserService userService;

	@Autowired
	private JwtSigninFilter jwtSigninFilter;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	private JwtAuthenticationProvider jwtAuthenticationProvider;

	@Autowired
	private ApiAuthenticationEntryPoint apiAuthenticationEntryPoint;

	@Autowired
	private ApiAccessDeniedHandler accessDeniedHandler;

	@Value("#{securityProps['security.jwt.signer.mac'] ?: 'KHAN0405'}")
	private String macSignerSecretKey;

	public SecurityConfig() {
		super(false);
	}

	@Bean
	public PropertiesFactoryBean securityProps() {
		PropertiesFactoryBean bean = new PropertiesFactoryBean();
		bean.setIgnoreResourceNotFound(true);
		bean.setLocation(new ClassPathResource("security.properties"));
		return bean;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.authenticationProvider(jwtAuthenticationProvider)
				.userDetailsService(userService)
				.passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.exceptionHandling()
				.accessDeniedHandler(accessDeniedHandler)
				.authenticationEntryPoint(apiAuthenticationEntryPoint)
				.and()
				.addFilterAfter(jwtSigninFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.headers().and()
				.securityContext().and()
				.requestCache().and()
				.anonymous().and()
				.servletApi().and()
				.authorizeRequests()
				.antMatchers("/admin/api", "/admin/api/**", "/v2/api-docs").hasRole("ADMIN")
				.antMatchers("/", "/api", "/api/", "/api/auth/signIn",
						"/api/noauth/**", "/api/authAnonymous").permitAll()
				.antMatchers("/api/**").hasRole("USER");
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public MacSigner jwtMacSigner() {
		return new MacSigner(macSignerSecretKey);
	}
}
