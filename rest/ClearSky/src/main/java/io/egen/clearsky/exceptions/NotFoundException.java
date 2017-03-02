package io.egen.clearsky.exceptions;

public class NotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -8706126811256075460L;
	private Exception exception;
	
	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(String message, Exception exception) {
		super(message);
		this.exception = exception;
	}
	
	public Exception getException() { return exception; }
}