package com.uber.driveronboardingmanagement.interfaces.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.uber.driveronboardingmanagement.interfaces.dtos.driver.DriverIdDto;

/**
 * This converter class allows us to use DriverIdDto as the type of
 * a @PathVariable parameter in a Spring @RestController class.
 */
@Component
public class StringToDriverIdDtoConverter implements Converter<String, DriverIdDto> {
	@Override
	public DriverIdDto convert(String source) {
		DriverIdDto driverId = new DriverIdDto();
		driverId.setId(source);
		return driverId;
	}
}

