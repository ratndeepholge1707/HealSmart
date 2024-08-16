package com.healsmart.custom_exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.healsmart.dtos.Response;

@RestControllerAdvice@Order(Ordered.HIGHEST_PRECEDENCE)
public class CentralExceptionHandler {

	@ExceptionHandler(NoSuchEmployeeExistsException.class)
	public ResponseEntity<?> noSuchEmployeeFound(NoSuchEmployeeExistsException e){
		return Response.success("NO_EMPLOYEE_FOUND");
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> allException(Exception e){
		return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
	}

}
