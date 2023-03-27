package com.uber.driverdataregistry.tests.domain.driver;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import com.uber.driverdataregistry.domain.driver.Address;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.uber.driverdataregistry.domain.driver.Vehicle;
import com.uber.driverdataregistry.domain.driver.DriverAggregateRoot;
import com.uber.driverdataregistry.domain.driver.DriverProfileEntity;
import com.uber.driverdataregistry.tests.TestUtils;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class DriverAggregateRootTests {
	private DriverAggregateRoot driverA;

	@Before
	public void setUp() {
		driverA = TestUtils.createTestDriver("poiuyqwert", "Ram", "Malhotra",
				TestUtils.createDate(1, Calendar.JANUARY, 1990), "Hyundai i20", "8640", "Bangalore",
				"ram@example.com", "7899886688");
	}

	@Test
	public void whenVehicleChanges_updateCurrentVehicle() {
		final String oldVehicleName = "Hyundai i20";
		final String oldLicensePlateNumber = "8640";
		final String oldRegisteredCity = "Bangalore";
		final String newVehicleName = "Toyota";
		final String newLicensePlateNumber = "1234";
		final String newRegisteredCity = "Bangalore";

		Vehicle newVehicle = new Vehicle(newVehicleName, newLicensePlateNumber, newRegisteredCity);
		driverA.moveToVehicle(newVehicle);
		assertEquals(2, driverA.getDriverProfile().getVehicleList().size());

		Vehicle oldVehicle = driverA.getDriverProfile().getVehicleList().iterator().next();
		assertEquals(oldVehicleName, oldVehicle.getVehicleName());
		assertEquals(oldLicensePlateNumber, oldVehicle.getLicensePlateNumber());
		assertEquals(oldRegisteredCity, oldVehicle.getRegisteredCity());

		assertEquals(newVehicleName, driverA.getDriverProfile().getCurrentVehicle().getVehicleName());
		assertEquals(newLicensePlateNumber, driverA.getDriverProfile().getCurrentVehicle().getLicensePlateNumber());
		assertEquals(newRegisteredCity, driverA.getDriverProfile().getCurrentVehicle().getRegisteredCity());
	}

	@Test
	public void whenVehicleDoesntChange_dontUpdateVehicleList() {
		final String oldVehicleName = "Hyundai i20";
		final String oldLicensePlateNumber = "8640";
		final String oldRegisteredCity = "Bangalore";

		Vehicle oldVehicle = new Vehicle(oldVehicleName, oldLicensePlateNumber, oldRegisteredCity);
		driverA.moveToVehicle(oldVehicle);
		assertEquals(1, driverA.getDriverProfile().getVehicleList().size());

		assertEquals(oldVehicleName, driverA.getDriverProfile().getCurrentVehicle().getVehicleName());
		assertEquals(oldLicensePlateNumber, driverA.getDriverProfile().getCurrentVehicle().getLicensePlateNumber());
		assertEquals(oldRegisteredCity, driverA.getDriverProfile().getCurrentVehicle().getRegisteredCity());
	}

	@Test
	public void whenExistingDriverId_thenUpdateDriverProfile() {
		final String newFirstname = "Rahul";
		final String newLastname = "Singh";
		final Date newBirthday = TestUtils.createDate(1, Calendar.JANUARY, 1990);
		final String oldVehicleName = "Hyundai i20";
		final String oldLicensePlateNumber = "8640";
		final String oldRegisteredCity = "Bangalore";
		final String newVehicleName = "Toyota";
		final String newLicensePlateNumber = "1234";
		final String newRegisteredCity = "Bangalore";
		final String newEmail = "rahul@example.com";
		final String newPhoneNumber = "099055 22241";

		Vehicle newVehicle = new Vehicle(newVehicleName, newLicensePlateNumber, newRegisteredCity);
		DriverProfileEntity updatedDriverProfile = new DriverProfileEntity(newFirstname, newLastname, newBirthday, newVehicle, newEmail, newPhoneNumber, new Address());

		driverA.updateDriverProfile(updatedDriverProfile);

		DriverProfileEntity driverProfile = driverA.getDriverProfile();
		assertEquals(newFirstname, driverProfile.getFirstname());
		assertEquals(newLastname, driverProfile.getLastname());
		assertEquals(newBirthday, driverProfile.getBirthday());
		assertEquals(newVehicleName, driverProfile.getCurrentVehicle().getVehicleName());
		assertEquals(newLicensePlateNumber, driverProfile.getCurrentVehicle().getLicensePlateNumber());
		assertEquals(newRegisteredCity, driverProfile.getCurrentVehicle().getRegisteredCity());
		assertEquals(newEmail, driverProfile.getEmail());
		assertEquals(newPhoneNumber, driverProfile.getPhoneNumber());

		assertEquals(2, driverA.getDriverProfile().getVehicleList().size());

		Vehicle oldVehicle = driverA.getDriverProfile().getVehicleList().iterator().next();
		assertEquals(oldVehicleName, oldVehicle.getVehicleName());
		assertEquals(oldLicensePlateNumber, oldVehicle.getLicensePlateNumber());
		assertEquals(oldRegisteredCity, oldVehicle.getRegisteredCity());
	}
}
