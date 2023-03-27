package com.uber.driverdataregistry.interfaces.dtos.driver;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

/**
 * The PaginatedDriverResponseDto holds a collection of DriverResponseDto
 */
public class PaginatedDriverResponseDto extends RepresentationModel {
	private final String filter;
	private final int limit;
	private final int offset;
	private final int size;

	private final List<DriverResponseDto> drivers;

	public PaginatedDriverResponseDto(String filter, int limit, int offset, int size,
			List<DriverResponseDto> drivers) {
		this.filter = filter;
		this.limit = limit;
		this.offset = offset;
		this.size = size;
		this.drivers = drivers;
	}

	public List<DriverResponseDto> getDrivers() {
		return drivers;
	}

	public String getFilter() {
		return filter;
	}

	public int getLimit() {
		return limit;
	}

	public int getOffset() {
		return offset;
	}

	public int getSize() {
		return size;
	}
}