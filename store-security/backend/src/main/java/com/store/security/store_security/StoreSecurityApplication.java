package com.store.security.store_security;

import com.store.security.store_security.properties.StoreProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;


@SpringBootApplication
@EnableAspectJAutoProxy
@EnableConfigurationProperties(value = {StoreProperties.class})
@EnableMethodSecurity(jsr250Enabled = true,securedEnabled = true)
@OpenAPIDefinition(
		info = @Info(
				title ="Store Security",
				description = "This is a store security application",
				version = "v1",
				contact = @Contact(
						name = "Simone Meneghetti",
						email = "persolenom@gmail.com"
				)
		)
)
public class StoreSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreSecurityApplication.class, args);
	}

}
