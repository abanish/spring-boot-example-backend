package com.uber.driverdataregistry.infrastructure;

import com.uber.driverdataregistry.domain.driver.DriverAggregateRoot;
import com.uber.driverdataregistry.domain.driver.DriverId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<DriverAggregateRoot, DriverId> {

	default DriverId nextId() {
		return DriverId.random();
	}
}
