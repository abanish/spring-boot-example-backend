package com.uber.driveronboardingmanagement.interfaces.dtos.driver;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

/**
 * The PaginatedDriverResponseDto holds a collection of DriverDto
 * with additional metadata parameters such as the limit, offset and size that
 * are used in the <a href=
 * "https://www.microservice-api-patterns.org/patterns/structure/compositeRepresentations/Pagination">Pagination</a>
 * pattern, specifically the <em>Offset-Based</em> Pagination variant.
 */
public class PaginatedDriverResponseDto extends RepresentationModel {
	private String filter;
	private int limit;
	private int offset;
	private int size;
	private List<DriverDto> drivers;

	public PaginatedDriverResponseDto() {}

	public PaginatedDriverResponseDto(String filter, int limit, int offset, int size, List<DriverDto> drivers) {
		this.filter = filter;
		this.limit = limit;
		this.offset = offset;
		this.size = size;
		this.drivers = drivers;
	}

	public List<DriverDto> getDrivers() {
		return drivers;
	}

	public void setDrivers(List<DriverDto> drivers) {
		this.drivers = drivers;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}