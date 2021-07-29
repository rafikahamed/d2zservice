package com.d2z.d2zservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

import com.d2z.d2zservice.exception.CustomResponseErrorHandler;

@Configuration
public class RestTemplateConfig {

	@Bean
	RestTemplate template() {
		return new RestTemplateBuilder()
	            .errorHandler(new CustomResponseErrorHandler())
	            .build();
	}
}
