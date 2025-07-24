package com.store.security.store_security.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StoreSecurityListener {


    @EventListener
    public void onAuthenticationSuccessEvent(AuthenticationSuccessEvent success)
    {
        log.info("Authentication success for: {}",success.getAuthentication().getName());
    }

    @EventListener
    public void onAuthenticationFailureEvent(AbstractAuthenticationFailureEvent failure)
    {
        log.info("Authentication failure for: {}",failure.getAuthentication().getName());

    }

    @EventListener
    public void onLogoutSuccessEvent(LogoutSuccessEvent logoutSuccessEvent)
    {
        log.info("Logout success for: {}",logoutSuccessEvent.getAuthentication().getName());
    }
}
