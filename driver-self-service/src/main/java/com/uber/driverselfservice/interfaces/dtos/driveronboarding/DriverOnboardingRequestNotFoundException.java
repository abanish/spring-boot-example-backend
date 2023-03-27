package com.uber.driverselfservice.interfaces.dtos.driveronboarding;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class DriverOnboardingRequestNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 7849369545115229681L;

	public DriverOnboardingRequestNotFoundException(String errorMessage) {
		super(errorMessage);
	}
}
