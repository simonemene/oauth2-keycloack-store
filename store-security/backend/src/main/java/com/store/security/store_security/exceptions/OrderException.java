package com.store.security.store_security.exceptions;

public class OrderException extends Exception{


	public OrderException(String message)
	{
		super(message);
	}

	public OrderException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
