/*package com.store.security.store_security.listener;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.AllArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class HttpSessionListenerImpl implements HttpSessionListener {

	private final SessionRegistry sessionRegistry;

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		System.out.println("Session destroyed: " + se.getSession().getId());
		sessionRegistry.removeSessionInformation(se.getSession().getId());
	}
}*/

