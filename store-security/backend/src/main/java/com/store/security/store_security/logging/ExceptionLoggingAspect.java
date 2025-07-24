package com.store.security.store_security.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExceptionLoggingAspect {

	@AfterThrowing(pointcut = "execution(* com.store.security.store_security.controller..*.*(..))", throwing = "exception")
	public void logException(Exception exception) {
		log.error("Exception {}",exception.getMessage());
	}
}
