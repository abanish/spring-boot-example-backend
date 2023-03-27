package com.uber.driveronboardingmanagement.interfaces.dtos.driver;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * You might be wondering why this Exception class is in the dtos package. Exceptions of this type
 * are thrown whenever the client sends a request with an invalid driver id. Spring will then
 * convert this exception into an HTTP 404 response.
 * */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class DriverNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -3416956458510420831L;

	public DriverNotFoundException(String errorMessage) {
		super(errorMessage);
	}
}