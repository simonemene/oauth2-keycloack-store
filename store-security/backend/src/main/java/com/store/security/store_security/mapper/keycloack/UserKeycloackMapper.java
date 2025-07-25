package com.store.security.store_security.mapper.keycloack;

import com.store.security.store_security.dto.UserDto;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserKeycloackMapper {

	public UserRepresentation userKeycloackMapper(UserDto userDto)
	{
		UserRepresentation user = new UserRepresentation();
		user.setUsername(userDto.getUsername());
		user.setCredentials(List.of(credentialKeycloackMapper(userDto.getPassword())));
		user.setEnabled(true);
		user.setEmail(userDto.getUsername());
		user.setEmailVerified(true);
		return user;

	}

	private CredentialRepresentation credentialKeycloackMapper(String password)
	{
		CredentialRepresentation credential = new CredentialRepresentation();
		credential.setTemporary(false);
		credential.setType(CredentialRepresentation.PASSWORD);
		credential.setValue(password);
		return credential;
	}
}
