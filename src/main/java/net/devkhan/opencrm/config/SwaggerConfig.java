package net.devkhan.opencrm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableWebMvc
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(springfox.documentation.builders.PathSelectors.regex("/api/.*"))
				.build()
				.apiInfo(apiInfo());
	}


	private ApiInfo apiInfo() {
		Contact contact = new Contact("mytpro", "http://www.dev-khan.net", "khan4257@gmail.com");
		ApiInfo apiInfo = new ApiInfo("Api Documentation", "Api Documentation", "1.0", "urn:tos",
				contact, "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0");
		return apiInfo;
	}
}
