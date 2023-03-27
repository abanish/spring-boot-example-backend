package com.uber.driverselfservice.interfaces.dtos.driver;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_GATEWAY)
public class DriverDataRegistryNotAvailableException extends RuntimeException {
	private static final long serialVersionUID = -156378720396633916L;

	public DriverDataRegistryNotAvailableException(String errorMessage) {
		super(errorMessage);
	}
}