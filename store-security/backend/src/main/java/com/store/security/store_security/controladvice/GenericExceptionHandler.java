package com.store.security.store_security.controladvice;

import com.store.security.store_security.exceptions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(OrderException.class)
	public ResponseEntity<String> controlOrderFailed(OrderException orderException)
	{
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(orderException.getMessage());
	}


	@ExceptionHandler(ArticleException.class)
	public ResponseEntity<String> controlArticleFailed(ArticleException articleException)
	{
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				articleException.getMessage());
	}


	@ExceptionHandler(StockException.class)
	public ResponseEntity<String> controlStockFailed(StockException stockException)
	{
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(stockException.getMessage());
	}

	@ExceptionHandler(UserException.class)
	public ResponseEntity<String> controlUserFailed(UserException userException)
	{
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userException.getMessage());
	}

	@ExceptionHandler(TrackException.class)
	public ResponseEntity<String> controlTrackFailer(TrackException userException)
	{
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userException.getMessage());
	}

	@ExceptionHandler(Throwable.class)
	public ResponseEntity<String> genericException(Throwable throwable)
	{
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(throwable.getCause().getMessage());
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		Map<String,String> errors = new HashMap<>();
		List<ObjectError> error = ex.getBindingResult().getAllErrors();
		for(ObjectError objectError : error)
		{
			String field = ((FieldError) error) .getField();
			String message = objectError.getDefaultMessage();
			errors.put(field,message);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}


}
