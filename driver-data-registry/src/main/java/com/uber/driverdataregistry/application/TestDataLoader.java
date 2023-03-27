package com.uber.driverdataregistry.application;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.uber.driverdataregistry.domain.driver.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.uber.driverdataregistry.infrastructure.DriverRepository;

/**
 * initial automated data load for testing
 * */
@Component
@Profile("!test")
public class TestDataLoader implements ApplicationRunner {
	@Autowired
	private DriverRepository driverRepository;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private List<DriverAggregateRoot> createDriversFromDummyData(List<Map<String, String>> driverData) {
		SimpleDateFormat birthdayFormat = new SimpleDateFormat("MM/dd/yyyy");
		List<DriverAggregateRoot> drivers = driverData.stream().map(data -> {
			final DriverId driverId = new DriverId(data.get("id"));
			final String firstName = data.get("first_name");
			final String lastName = data.get("last_name");
			Date birthday = null;
			try {
				birthday = birthdayFormat.parse(data.get("birthday"));
			} catch (ParseException e) {
			}
			final Vehicle currentVehicle = new Vehicle(data.get("vehicle_name"), data.get("license_number_plate"), data.get("registeredCity"));
			final String email = data.get("email");
			final String phoneNumber = data.get("phone_number");
			final DriverProfileEntity driverProfile = new DriverProfileEntity(firstName, lastName, birthday, currentVehicle, email, phoneNumber, new Address());
			return new DriverAggregateRoot(driverId, driverProfile);
		}).collect(Collectors.toList());
		return drivers;
	}

	private List<Map<String, String>> loadDrivers() {
		try(InputStream file = new ClassPathResource("mock_drivers_small.csv").getInputStream()) {
			CsvMapper mapper = new CsvMapper();
			CsvSchema schema = CsvSchema.emptySchema().withHeader();
			MappingIterator<Map<String, String>> readValues = mapper.readerFor(Map.class).with(schema).readValues(file);
			return readValues.readAll();
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

	@Override
	public void run(ApplicationArguments args) throws ParseException {
		if(driverRepository.count() > 0) {
			logger.info("The database already contains existing entities. Dataload skipped.");
			return;
		}

		List<Map<String, String>> driverData = loadDrivers();

		logger.info("Loaded " + driverData.size() + " drivers.");

		List<DriverAggregateRoot> drivers = createDriversFromDummyData(driverData);

		logger.info("Created " + driverData.size() + " drivers.");

		driverRepository.saveAll(drivers);

		logger.info("Inserted " + driverData.size() + " drivers into database.");

		logger.info("TestDataLoader has successfully imported all application dummy data.");
	}
}