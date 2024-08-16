package com.healsmart.custom_exception;

public class PatientAlreadyExistsException extends RuntimeException {

	public PatientAlreadyExistsException(String message) {
		super(message);
		
	}
	
	
}
