package com.store.security.store_security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "store")
public record StoreProperties(String securityAllowedOrigin,String jwtSecretKeyValue,String jwtHeader){
}
