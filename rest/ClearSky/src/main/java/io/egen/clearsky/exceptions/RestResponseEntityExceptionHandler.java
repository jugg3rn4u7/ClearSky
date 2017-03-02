package io.egen.clearsky.exceptions;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.egen.clearsky.entities.ExceptionResource;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	/**
	 * Description: Handle Client Errors (400 Bad Request)
	 * @param e
	 * @param request
	 * @return ResponseEntity<Object>
	 */
	@ExceptionHandler({ BadRequestException.class })
    protected ResponseEntity<Object> handleBadRequest(RuntimeException e, WebRequest request) {
		
		BadRequestException badRequestException = (BadRequestException) e;
		
		ExceptionResource exception;
		
		// If Exception object is present, use it !
		Optional<Exception> optionalExceptionObject = Optional.ofNullable(badRequestException.getException());
		if(optionalExceptionObject.isPresent()) {
			exception = new ExceptionResource(400, badRequestException.getMessage(), optionalExceptionObject.get().getMessage());
		} else {
			exception = new ExceptionResource(400, badRequestException.getMessage());
		}
		
		// Set Response Headers and content-type as application/json
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, exception, headers, HttpStatus.BAD_REQUEST, request);
    }
	
	/**
	 * Description: Handle Client Errors (404 Not Found)
	 * @param e
	 * @param request
	 * @return ResponseEntity<Object>
	 */
	@ExceptionHandler({ NotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(RuntimeException e, WebRequest request) {
		
		NotFoundException notFoundException = (NotFoundException) e;
		
		ExceptionResource exception;
		
		// If Exception object is present, use it !
		Optional<Exception> optionalExceptionObject = Optional.ofNullable(notFoundException.getException());
		if(optionalExceptionObject.isPresent()) {
			exception = new ExceptionResource(404, notFoundException.getMessage(), optionalExceptionObject.get().getMessage());
		} else {
			exception = new ExceptionResource(404, notFoundException.getMessage());
		}
		
		// Set Response Headers and content-type as application/json
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, exception, headers, HttpStatus.NOT_FOUND, request);
    }
	
	/**
	 * Description: Handle Server Errors (500 Internal Server Error)
	 * @param e
	 * @param request
	 * @return ResponseEntity<Object>
	 */
	@ExceptionHandler({ InternalServerError.class, NullPointerException.class, IntrospectionException.class, IllegalAccessException.class, IllegalArgumentException.class, InvocationTargetException.class })
    protected ResponseEntity<Object> handleInternalServer(RuntimeException e, WebRequest request) {
		
		InternalServerError internalServerError = (InternalServerError) e;
		
		ExceptionResource exception;
		
		// If Exception object is present, use it !
		Optional<Exception> optionalExceptionObject = Optional.ofNullable(internalServerError.getException());
		if(optionalExceptionObject.isPresent()) {
			exception = new ExceptionResource(500, internalServerError.getMessage(), optionalExceptionObject.get().getMessage());
		} else {
			exception = new ExceptionResource(500, internalServerError.getMessage());
		}
		
		// Set Response Headers and content-type as application/json
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, exception, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
