package com.uber.driverdataregistry.tests.domain.driver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.uber.driverdataregistry.domain.driver.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.uber.driverdataregistry.infrastructure.DriverRepository;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class DriverFactoryTests {
	private final static DriverId driverId = new DriverId("rgpp0wkpec");
	private final static String firstName = "Max";
	private final static String lastName = "Mustermann";
	private final static Date birthday = new GregorianCalendar(1990, Calendar.JANUARY, 1).getTime();
	private final static String registeredRegisteredCity = "Rapperswil";
	private final static String vehicleName = "Oberseestrasse 10";
	private final static String licensePlateNumber = "8640";
	private final static String formattedPhoneNumber = "091552 22411";
	private final static String unformattedPhoneNumber = "091552 22411";
	private final static String invalidPhoneNumber = "abc";
	private final static String email = "admin@example.com";

	@TestConfiguration
	static class DriverFactoryTestContextConfiguration {
		@Bean
		public DriverFactory driverFactory() {
			return new DriverFactory();
		}
	}

	@Autowired
	private DriverFactory driverFactory;

	@MockBean
	private DriverRepository driverRepository;

	@Before
	public void setUp() {
		Mockito.when(driverRepository.nextId()).thenReturn(driverId);
	}

	@Test
	public void whenValidPhoneNumber_thenDriverRootEntityShouldBeCreated() {
		Vehicle vehicle = new Vehicle(vehicleName, licensePlateNumber, registeredRegisteredCity);
		DriverProfileEntity profile1 = new DriverProfileEntity(firstName, lastName, birthday, vehicle, email, formattedPhoneNumber, new Address());
		DriverAggregateRoot driver = driverFactory.create(profile1);

		assertEquals(driverId, driver.getId());

		DriverProfileEntity profile2 = driver.getDriverProfile();
		assertEquals(firstName, profile2.getFirstname());
		assertEquals(lastName, profile2.getLastname());
		assertEquals(birthday, profile2.getBirthday());
		assertEquals(registeredRegisteredCity, profile2.getCurrentVehicle().getRegisteredCity());
		assertEquals(vehicleName, profile2.getCurrentVehicle().getVehicleName());
		assertEquals(licensePlateNumber, profile2.getCurrentVehicle().getLicensePlateNumber());
		assertEquals(formattedPhoneNumber, profile2.getPhoneNumber());
		assertEquals(email, profile2.getEmail());
	}

	@Test
	public void whenValidPhoneNumber_thenPhoneNumberIsFormatted() {
		Vehicle vehicle = new Vehicle(vehicleName, licensePlateNumber, registeredRegisteredCity);
		DriverProfileEntity profile1 = new DriverProfileEntity(firstName, lastName, birthday, vehicle, email, unformattedPhoneNumber, new Address());
		DriverAggregateRoot driver = driverFactory.create(profile1);

		assertEquals(driverId, driver.getId());

		DriverProfileEntity profile2 = driver.getDriverProfile();
		assertEquals(firstName, profile2.getFirstname());
		assertEquals(lastName, profile2.getLastname());
		assertEquals(birthday, profile2.getBirthday());
		assertEquals(registeredRegisteredCity, profile2.getCurrentVehicle().getRegisteredCity());
		assertEquals(vehicleName, profile2.getCurrentVehicle().getVehicleName());
		assertEquals(licensePlateNumber, profile2.getCurrentVehicle().getLicensePlateNumber());
		assertEquals(formattedPhoneNumber, profile2.getPhoneNumber());
		assertEquals(email, profile2.getEmail());
	}

	@Test
	public void whenInvalidPhoneNumber_thenAssertionErrorIsThrown() {
		boolean didThrowError = false;
		try {
			Vehicle vehicle = new Vehicle(vehicleName, licensePlateNumber, registeredRegisteredCity);
			DriverProfileEntity profile = new DriverProfileEntity(firstName, lastName, birthday, vehicle, email, invalidPhoneNumber, new Address());
			driverFactory.create(profile);
		} catch(AssertionError error) {
			didThrowError = true;
		}

		if(!didThrowError) {
			fail();
		}
	}
}