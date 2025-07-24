package com.store.security.store_security.provider;

import com.store.security.store_security.service.UserSecurityDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserProviderDetailsManager implements AuthenticationProvider {

    private final UserSecurityDetailService userSecurityDetailService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        if (passwordEncoder.matches(password, userSecurityDetailService.loadUserByUsername(username).getPassword())) {
            return new UsernamePasswordAuthenticationToken(username, password, userSecurityDetailService.loadUserByUsername(username).getAuthorities());
        }
        throw new BadCredentialsException("Bad credentials");

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
