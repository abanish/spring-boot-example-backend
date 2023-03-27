package com.uber.driverselfservice.application;

import java.io.InputStream;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.uber.driverselfservice.domain.driver.DriverId;
import com.uber.driverselfservice.domain.identityaccess.UserLoginEntity;
import com.uber.driverselfservice.infrastructure.UserLoginRepository;

/**
 * The run() method of the DataLoader class is automatically executed and populates
 * the database with sample user logins.
 * */
@Component
@Profile("!test")
public class DataLoader implements ApplicationRunner {
	@Autowired
	private UserLoginRepository userRepository;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private List<UserLoginEntity> createUserLoginsFromDummyData(List<Map<String, String>> userData) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return userData.stream().map(data -> {
			final String email = data.get("email");
			final String hashedPassword = passwordEncoder.encode(data.get("password"));
			final DriverId driverId = new DriverId(data.get("id"));
			return new UserLoginEntity(email, hashedPassword, "USER", driverId);
		}).collect(Collectors.toList());
	}

	private List<Map<String, String>> loadUsers() {
		try(InputStream file = new ClassPathResource("mock_users_small.csv").getInputStream()) {
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
		if(userRepository.count() > 0) {
			logger.info("Skipping import of application dummy data, because the database already contains existing entities.");
			return;
		}

		List<Map<String, String>> userData = loadUsers();

		logger.info("Loaded " + userData.size() + " users.");

		List<UserLoginEntity> userLogins = createUserLoginsFromDummyData(userData);

		logger.info("Created " + userLogins.size() + " user logins.");

		userRepository.saveAll(userLogins);

		logger.info("Inserted " + userLogins.size() + " user logins into database.");

		logger.info("DataLoader has successfully imported all application dummy data, the application is now ready.");
	}
}