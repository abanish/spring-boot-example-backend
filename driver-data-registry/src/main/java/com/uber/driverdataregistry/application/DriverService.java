package com.uber.driverdataregistry.application;

import com.uber.driverdataregistry.domain.driver.*;
import com.uber.driverdataregistry.infrastructure.DriverRepository;
import com.uber.driverdataregistry.interfaces.dtos.driver.DocumentDto;
import org.hibernate.jpa.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The DriverService class is an application service that is
 * responsible for creating, updating and retrieving driver entities.
 */

@Service
@Component
public class DriverService {
	@Autowired
	private DriverRepository driverRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private DriverFactory driverFactory;

	public Optional<DriverAggregateRoot> updateVehicle(DriverId driverId, Vehicle updatedVehicle) {
		Optional<DriverAggregateRoot> optDriver = driverRepository.findById(driverId);
		if (!optDriver.isPresent()) {
			return optDriver;
		}

		DriverAggregateRoot driver = optDriver.get();
		driver.moveToVehicle(updatedVehicle);
		driverRepository.save(driver);
		return optDriver;
	}

	public Optional<DriverAggregateRoot> verifyDriver(DriverId driverId) {
		Optional<DriverAggregateRoot> optDriver = driverRepository.findById(driverId);
		if (!optDriver.isPresent()) {
			return optDriver;
		}

		DriverAggregateRoot driver = optDriver.get();
		driver.getDriverProfile().setIsVerified(true);
		driver.getDriverProfile().getCurrentVehicle().setIsVerified(true);
		driverRepository.save(driver);
		return optDriver;
	}

	public Optional<DriverAggregateRoot> addDocument(DriverId driverId, MultipartFile file) throws IOException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		Document doc = new Document(fileName, file.getContentType(), file.getBytes());
		Optional<DriverAggregateRoot> optDriver = driverRepository.findById(driverId);
		if (!optDriver.isPresent()) {
			return optDriver;
		}

		DriverAggregateRoot driver = optDriver.get();
		driver.addDocument(doc);
		driverRepository.save(driver);
		return optDriver;
	}

	public Optional<DriverAggregateRoot> addDocumentSelfService(DriverId driverId, DocumentDto docDto) throws IOException {
		String fileName = StringUtils.cleanPath(docDto.getDocumentName());
		Document doc = new Document(fileName, docDto.getDocumentType(), docDto.getData());
		Optional<DriverAggregateRoot> optDriver = driverRepository.findById(driverId);
		if (!optDriver.isPresent()) {
			return optDriver;
		}

		DriverAggregateRoot driver = optDriver.get();
		driver.addDocument(doc);
		driverRepository.save(driver);
		return optDriver;
	}

	public Optional<DriverAggregateRoot> updateDriverProfile(DriverId driverId, DriverProfileEntity updatedDriverProfile) {
		Optional<DriverAggregateRoot> optDriver = driverRepository.findById(driverId);
		if (!optDriver.isPresent()) {
			return optDriver;
		}

		DriverAggregateRoot driver = optDriver.get();
		driver.updateDriverProfile(updatedDriverProfile);
		driverRepository.save(driver);
		return optDriver;
	}

	public DriverAggregateRoot createDriver(DriverProfileEntity driverProfile) {
		DriverAggregateRoot driver = driverFactory.create(driverProfile);
		driverRepository.save(driver);
		return driver;
	}

	public List<DriverAggregateRoot> getDrivers(String ids) {
		List<DriverId> driverIds = Arrays.stream(ids.split(",")).map(id -> new DriverId(id.trim())).collect(Collectors.toList());

		List<DriverAggregateRoot> drivers = new ArrayList<>();
		for (DriverId driverId : driverIds) {
			Optional<DriverAggregateRoot> driver = driverRepository.findById(driverId);
			driver.ifPresent(drivers::add);
		}
		return drivers;
	}

	public Page<DriverAggregateRoot> getDrivers(String filter, int limit, int offset) {
		String filterParameter = "%" + filter + "%";

		long totalSize = entityManager.createQuery(
						"select count(1) from DriverAggregateRoot c " +
								"left join fetch c.driverProfile " +
								"where c.driverProfile.firstname like :filter or c.driverProfile.lastname like :filter", Long.class)
				.setParameter("filter", filterParameter)
				.getSingleResult();

		List<DriverId> driverIds = entityManager.createQuery(
						"select c.id from DriverAggregateRoot c " +
								"left join fetch c.driverProfile " +
								"where c.driverProfile.firstname like :filter or c.driverProfile.lastname like :filter " +
								"order by c.driverProfile.firstname, c.driverProfile.lastname", DriverId.class)
				.setParameter("filter", filterParameter)
				.setFirstResult(offset)
				.setMaxResults(limit)
				.getResultList();

		List<DriverAggregateRoot> driverAggregateRoots = entityManager.createQuery(
						"select c from DriverAggregateRoot c " +
								"left join fetch c.driverProfile " +
								"left join fetch c.driverProfile.vehicleList " +
								"where c.id in (:driverIds) " +
								"order by c.driverProfile.firstname, c.driverProfile.lastname", DriverAggregateRoot.class)
				.setParameter("driverIds", driverIds)
				.setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
				.getResultList();

		return new Page<>(driverAggregateRoots, offset, limit, (int) totalSize);
	}
}
