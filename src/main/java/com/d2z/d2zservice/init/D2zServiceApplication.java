package com.d2z.d2zservice.init;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.github.ulisesbocchio.jar.resources.JarResourceLoader;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
@EnableAutoConfiguration 
@ComponentScan({ "com.d2z.d2zservice.*"})
@EnableJpaRepositories(basePackages="com.d2z.d2zservice.repository")
@EnableTransactionManagement
@EnableScheduling
@EntityScan(basePackages="com.d2z.d2zservice.entity")
public class D2zServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(D2zServiceApplication.class, args);
		
//		 StandardEnvironment environment = new StandardEnvironment(); new
//		  SpringApplicationBuilder() .sources(D2zServiceApplication.class)
//		  .environment(environment) .resourceLoader(new JarResourceLoader(environment,
//		  "resources.extract.dir")) .build() .run(args);
		try {
		    String result = InetAddress.getLocalHost().getHostName();
		    if (StringUtils.isNotEmpty( result))
		        System.out.println("Service HOST Name --->"+result);
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
