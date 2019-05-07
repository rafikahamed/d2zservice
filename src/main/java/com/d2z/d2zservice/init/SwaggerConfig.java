package com.d2z.d2zservice.init;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {  
	
	@Bean
	public Docket merchantApi() {
	        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().paths(regex("/v1/.*"))
	                .build();
	}
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("D2Z Dashboard").description("d2z report")
                .contact("RAFIK AHAMED").version("2.0").build();
    }
}

