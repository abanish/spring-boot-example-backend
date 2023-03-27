package com.uber.driveronboardingmanagement.domain.driveronboarding;

/**
 * A RequestStatus is a value object that is used to represent
 * the current status of a driver onboarding request.
 */
public enum RequestStatus  {
	ONBOARDING_REQUEST_SUBMITTED,
	ONBOARDING_REQUEST_EXPIRED,

	ONBOARDING_REQUEST_REJECTED,

	BACKGROUND_VERIFICATION_SUCCESS,

	BACKGROUND_VERIFICATION_FAILED,

	VEHICLE_VERIFICATION_SUCCESS,

	VEHICLE_VERIFICATION_FAILED,
	ONBOARDING_REQUEST_SUCCESS,

	DEVICE_REGISTRATION_INITIATED,

	DEVICE_SHIPPED,

	DEVICE_DELIVERED;


	public boolean canTransitionTo(RequestStatus newStatus) {
		/*switch(this) {
			case ONBOARDING_REQUEST_SUBMITTED:
				return newStatus == ONBOARDING_REQUEST_REJECTED || newStatus == BACKGROUND_VERIFICATION_FAILED || newStatus == BACKGROUND_VERIFICATION_SUCCESS;
			case BACKGROUND_VERIFICATION_SUCCESS:
				return newStatus == VEHICLE_VERIFICATION_SUCCESS || newStatus == VEHICLE_VERIFICATION_FAILED;
			case VEHICLE_VERIFICATION_SUCCESS:
				return newStatus == ONBOARDING_REQUEST_SUCCESS || newStatus == DEVICE_REGISTRATION_INITIATED || newStatus == DEVICE_DELIVERED || newStatus == DEVICE_SHIPPED;
			case DEVICE_DELIVERED:
			case BACKGROUND_VERIFICATION_FAILED:
			case VEHICLE_VERIFICATION_FAILED:
			case ONBOARDING_REQUEST_REJECTED:
				return false;
			default:
				return false;
		}*/
		return true;
	}
}