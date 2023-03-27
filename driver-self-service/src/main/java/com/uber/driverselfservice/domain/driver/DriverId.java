package com.uber.driverselfservice.domain.driver;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * A DriverId is a value object that is used to represent the unique id of a driver.
 */
@Embeddable
public class DriverId implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;

	public DriverId() {
		this.setId(null);
	}

	public DriverId(String id) {
		this.setId(id);
	}

	public static DriverId random() {
		return new DriverId(RandomStringUtils.randomAlphanumeric(10).toLowerCase());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		DriverId other = (DriverId) obj;
		return Objects.equals(getId(), other.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return getId();
	}
}
