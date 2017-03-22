package io.egen.clearskyboot.exceptions;

public class InternalServerError extends RuntimeException {
	
	private static final long serialVersionUID = -4610974200577360499L;
	private Exception exception;
	
	public InternalServerError(String message) {
		super(message);
	}

	public InternalServerError(String message, Exception exception) {
		super(message);
		this.exception = exception;
	}
	
	public Exception getException() { return exception; }
}
