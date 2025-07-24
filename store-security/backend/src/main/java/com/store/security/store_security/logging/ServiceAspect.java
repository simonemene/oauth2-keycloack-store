package com.store.security.store_security.logging;

import com.store.security.store_security.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class ServiceAspect {

	@Around(value = "@annotation(com.store.security.store_security.annotation.LogExecutionTime)")
	public Object userLoginExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = null;
		ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
		if(null != attributes)
		{
			request = attributes.getRequest();
		}
		Object[] args = joinPoint.getArgs();
		long start = System.currentTimeMillis();
		Object result = joinPoint.proceed();
		long end = System.currentTimeMillis();
		if(null != request)
		{
			if(args[0] instanceof UserDto userDto)
			{
				log.info("REQUEST: {} - {}," +
								"username : {}," +
								" Execution time: {} ms",request.getMethod(),request.getRequestURI(),
						userDto.getUsername(),(end-start));
			}else
			{
				log.info("REQUEST: {} - {}," +
								"Argument : {}," +
								" Execution time: {} ms",request.getMethod(),
						request.getRequestURI(),
						Arrays.toString(args),(end-start));
			}

		}
		else
		{
			log.info("Argument : {}," +
					"Execution time: {} ms",
					Arrays.toString(args),(end-start));
		}
		return result;
	}
}
