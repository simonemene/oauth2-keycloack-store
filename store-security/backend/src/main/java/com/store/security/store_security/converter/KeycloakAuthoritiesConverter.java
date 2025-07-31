package com.store.security.store_security.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

	@Override
	public Collection<GrantedAuthority> convert(Jwt source) {

       Map<String,Object> realm_access = (Map<String, Object>)source.getClaims().get("realm_access");

	   if(null == realm_access || realm_access.isEmpty())
	   {
		   return List.of();
	   }
	   return ((List<String>) realm_access.get("roles")).stream()
			   .map(SimpleGrantedAuthority::new)
			   .collect(Collectors.toList());
	}
}
