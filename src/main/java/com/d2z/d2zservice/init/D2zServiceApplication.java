package com.d2z.d2zservice.init;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
@EnableAutoConfiguration 
@EnableEncryptableProperties
@ComponentScan({ "com.d2z.d2zservice.*"})
@EnableJpaRepositories(basePackages="com.d2z.d2zservice.repository")
@EnableTransactionManagement
@EnableScheduling
@EnableCaching
@EntityScan(basePackages="com.d2z.d2zservice.entity")
public class D2zServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(D2zServiceApplication.class, args);
		
		try {
		    String result = InetAddress.getLocalHost().getHostName();
		    if (StringUtils.isNotEmpty( result))
		        System.out.println("Service HOST Name sample --->"+result);
		} catch (UnknownHostException e) {
		}
	}
	
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE").allowedOrigins("*")
                        .allowedHeaders("*");
            }
        };
    }
	
}
