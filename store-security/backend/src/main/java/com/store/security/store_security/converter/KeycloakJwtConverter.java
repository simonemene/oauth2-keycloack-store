package com.store.security.store_security.converter;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

public class KeycloakJwtConverter extends JwtAuthenticationConverter {

	public KeycloakJwtConverter()
	{
		this.setJwtGrantedAuthoritiesConverter(new KeycloakAuthoritiesConverter());
		this.setPrincipalClaimName("preferred_username");
	}
}
