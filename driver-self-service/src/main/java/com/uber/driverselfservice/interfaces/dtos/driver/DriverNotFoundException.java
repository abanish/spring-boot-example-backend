package com.uber.driverselfservice.interfaces.dtos.driver;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class DriverNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -402909143828880299L;

	public DriverNotFoundException(String errorMessage) {
		super(errorMessage);
	}
}