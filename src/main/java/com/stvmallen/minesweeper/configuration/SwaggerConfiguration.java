package com.stvmallen.minesweeper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
	@Bean
	public Docket requiredFieldsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis(RequestHandlerSelectors.basePackage("com.stvmallen.minesweeper"))
			.build()
			.useDefaultResponseMessages(false)
			.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
			.title("Minesweeper service REST API")
			.description("Microservice that handles requests to play the Minesweeper game")
			.contact(new Contact("Steve Mallen", "https://github.com/esteban-mallen", "estebanmallen@gmail.com"))
			.license("MIT")
			.version("1.0")
			.build();
	}

}
