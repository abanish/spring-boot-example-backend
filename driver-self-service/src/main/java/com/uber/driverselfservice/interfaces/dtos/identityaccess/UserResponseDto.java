package com.uber.driverselfservice.interfaces.dtos.identityaccess;

/**
 * UserResponseDto is a data transfer object (DTO) that represents a user. The driverId property references the driver object in the
 * Driver Core that belongs to this user. A driverId that is set to null indicates that the user has not yet completed the registration.
 * */
public class UserResponseDto {
	private final String email;
	private final String driverId;

	public UserResponseDto(String email, String driverId) {
		this.email = email;
		this.driverId = driverId;
	}

	public String getEmail() {
		return email;
	}

	public String getDriverId() {
		return driverId;
	}
}
