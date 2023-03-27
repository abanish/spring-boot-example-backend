package com.uber.driverdataregistry.domain.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.uber.driverdataregistry.infrastructure.DriverRepository;

/**
 * DriverFactory is a factory that is responsible for the creation of DriverAggregateRoot objects.
 * It makes sure that each new driver has a unique DriverId and that phone numbers are formatted
 * correctly.
 * */
@Component
public class DriverFactory {
	@Autowired
	private DriverRepository driverRepository;

	public DriverAggregateRoot create(DriverProfileEntity driverProfile) {
		DriverId id = driverRepository.nextId();
		driverProfile.setPhoneNumber(formatPhoneNumber(driverProfile.getPhoneNumber()));
		return new DriverAggregateRoot(id, driverProfile);
	}

	public static String formatPhoneNumber(String phoneNumberStr) {
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		try {
			PhoneNumber phoneNumber = phoneUtil.parse(phoneNumberStr, "IN");
			return phoneUtil.format(phoneNumber, PhoneNumberFormat.NATIONAL);
		} catch (NumberParseException e) {
			throw new AssertionError();
		}
	}
}
