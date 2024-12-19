package com.rmgYantra.loginapp.exceptions;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{
	
	
	

	@ExceptionHandler(Exception.class)
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
	 return new ResponseEntity(exceptionResponse,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(UserNameAlreadyPresentException.class)
	protected ResponseEntity<Object> handleUserNameAlreadyPresentException(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
	 return new ResponseEntity(exceptionResponse,HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(ProjectNameAlreadyPresentException.class)
	protected ResponseEntity<Object> handleProjectNameAlreadyPresentException(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
	 return new ResponseEntity(exceptionResponse,HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	protected ResponseEntity<Object> handleResourceNotFoundException(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
	 return new ResponseEntity(exceptionResponse,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(TransactionIdAlreadyExistsException.class)
	protected ResponseEntity<Object> handleTransactionIdAlreadyExistsException(TransactionIdAlreadyExistsException ex) {
		//ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
	 return new ResponseEntity(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	

}
