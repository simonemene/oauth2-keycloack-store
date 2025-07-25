package com.store.security.store_security.config;

import com.store.security.store_security.properties.KeycloackProperties;
import lombok.AllArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class KeycloackConfig {

	private final KeycloackProperties keycloackProperties;

	@Bean
	public Keycloak keycloak()
	{
		return KeycloakBuilder.builder()
				.serverUrl(keycloackProperties.endpoint())
				.realm(keycloackProperties.realm())
				.clientId(keycloackProperties.application().clientId())
				.clientSecret(keycloackProperties.application().secret())
				.grantType(OAuth2Constants.CLIENT_CREDENTIALS)
				.build();
	}
}
