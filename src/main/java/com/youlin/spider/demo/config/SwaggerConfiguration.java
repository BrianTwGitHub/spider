package com.youlin.spider.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
	@Bean
	public OpenAPI customOpenApi() {
		return new OpenAPI().info(new Info()
			.title("104 爬蟲")
			.version("0.0.1-SNAPSHOT")
			.description("104 爬蟲"));
	}
}
