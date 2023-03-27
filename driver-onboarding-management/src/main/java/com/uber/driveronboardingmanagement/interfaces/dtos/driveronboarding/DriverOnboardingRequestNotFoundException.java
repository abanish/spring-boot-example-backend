package com.uber.driveronboardingmanagement.interfaces.dtos.driveronboarding;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * You might be wondering why this Exception class is in the dtos package. Exceptions of this type
 * are thrown whenever the client tries to fetch an driver onboarding request that doesn't exist. Spring will then
 * convert this exception into an HTTP 404 response.
 * */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class DriverOnboardingRequestNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 532390503974496190L;

	public DriverOnboardingRequestNotFoundException(String errorMessage) {
		super(errorMessage);
	}
}
