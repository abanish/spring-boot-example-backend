package com.uber.driveronboardingmanagement.infrastructure;

import java.util.List;

import com.uber.driveronboardingmanagement.domain.driver.DriverId;
import org.springframework.data.jpa.repository.JpaRepository;

import com.uber.driveronboardingmanagement.domain.driveronboarding.DriverOnboardingRequestAggregateRoot;

/**
 * The DriverOnboardingRequestRepository can be used to read and write DriverOnboardingRequestAggregateRoot objects from and to the backing database.
 * */
public interface DriverOnboardingRequestRepository extends JpaRepository<DriverOnboardingRequestAggregateRoot, Long> {
	List<DriverOnboardingRequestAggregateRoot> findByDriverInfo_DriverId(DriverId driverId);
	List<DriverOnboardingRequestAggregateRoot> findAllByOrderByDateDesc();
}