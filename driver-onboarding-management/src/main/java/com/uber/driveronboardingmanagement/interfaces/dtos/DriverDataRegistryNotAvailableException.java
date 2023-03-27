package com.uber.driveronboardingmanagement.interfaces.dtos;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * You might be wondering why this Exception class is in the dtos package. Exceptions of this type
 * are thrown whenever the Onboarding Management backend can't connect to the Driver Core. Spring will then
 * convert this exception into an HTTP 502 response.
 * */
@ResponseStatus(code = HttpStatus.BAD_GATEWAY)
public class DriverDataRegistryNotAvailableException extends RuntimeException {
	private static final long serialVersionUID = 2146599135907479601L;

	public DriverDataRegistryNotAvailableException(String errorMessage) {
		super(errorMessage);
	}
}