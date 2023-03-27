package com.uber.driverselfservice.interfaces.dtos.identityaccess;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = 5852316265258762036L;

	public UserAlreadyExistsException(String errorMessage) {
		super(errorMessage);
	}
}