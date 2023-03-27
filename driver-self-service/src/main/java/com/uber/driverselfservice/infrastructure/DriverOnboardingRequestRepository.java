package com.uber.driverselfservice.infrastructure;

import java.util.List;

import com.uber.driverselfservice.domain.driver.DriverId;
import com.uber.driverselfservice.domain.driveronboarding.DriverOnboardingRequestAggregateRoot;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The DriverOnboardingRequestRepository can be used to read and write DriverOnboardingRequestAggregateRoot objects from
 * and to the backing database.
 * */
public interface DriverOnboardingRequestRepository extends JpaRepository<DriverOnboardingRequestAggregateRoot, Long> {
	List<DriverOnboardingRequestAggregateRoot> findByDriverInfo_DriverIdOrderByDateDesc(DriverId driverId);
	List<DriverOnboardingRequestAggregateRoot> findAllByOrderByDateDesc();
}