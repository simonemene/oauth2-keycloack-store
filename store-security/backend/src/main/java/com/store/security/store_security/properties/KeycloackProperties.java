package com.store.security.store_security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloack")
public record KeycloackProperties(String realm,String endpoint,Application application) {
	public record Application(String clientId, String secret){}
}
