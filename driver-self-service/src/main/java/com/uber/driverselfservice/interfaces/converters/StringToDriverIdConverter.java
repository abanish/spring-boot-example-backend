package com.uber.driverselfservice.interfaces.converters;

import com.uber.driverselfservice.domain.driver.DriverId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * This converter class allows us to use DriverId as the type of
 * a @PathVariable parameter in a Spring @RestController class.
 */
@Component
public class StringToDriverIdConverter implements Converter<String, DriverId> {
	@Override
	public DriverId convert(String source) {
		return new DriverId(source);
	}
}
