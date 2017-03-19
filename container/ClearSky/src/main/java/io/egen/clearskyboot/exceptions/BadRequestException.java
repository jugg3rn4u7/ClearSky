package io.egen.clearskyboot.exceptions;

public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 133459562490364255L;
	private Exception exception;
	
	public BadRequestException(String message) {
		super(message);
	}

	public BadRequestException(String message, Exception exception) {
		super(message);
		this.exception = exception;
	}
	
	public Exception getException() { return exception; }
}