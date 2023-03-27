package com.uber.driverdataregistry.interfaces.converters;

import com.uber.driverdataregistry.domain.driver.DriverId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToDriverIdConverter implements Converter<String, DriverId> {
	@Override
	public DriverId convert(String source) {
		return new DriverId(source);
	}
}
