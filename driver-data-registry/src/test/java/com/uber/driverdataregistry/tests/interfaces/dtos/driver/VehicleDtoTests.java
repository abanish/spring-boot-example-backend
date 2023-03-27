package com.uber.driverdataregistry.tests.interfaces.dtos.driver;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import com.uber.driverdataregistry.interfaces.dtos.driver.VehicleDto;

@RunWith(SpringRunner.class)
@JsonTest
public class VehicleDtoTests {
	@Autowired
	private JacksonTester<VehicleDto> json;

	@Test
	public void deserializeJson() throws Exception {
		String content = "{\"vehicleName\":\"Hyundai i20\",\"licensePlateNumber\":\"8640\",\"registeredCity\":\"Bangalore\"}";
		VehicleDto vehicle = json.parseObject(content);
		assertThat(vehicle.getVehicleName()).isEqualTo("Hyundai i20");
		assertThat(vehicle.getLicensePlateNumber()).isEqualTo("8640");
		assertThat(vehicle.getRegisteredCity()).isEqualTo("Bangalore");
	}

	@Test
	public void serializeJson() throws Exception {
		VehicleDto vehicle = new VehicleDto("Hyundai i20", "8640", "Bangalore");
		assertJsonPropertyEquals(vehicle, "@.vehicleName", "Hyundai i20");
		assertJsonPropertyEquals(vehicle, "@.licensePlateNumber", "8640");
		assertJsonPropertyEquals(vehicle, "@.registeredCity", "Bangalore");
	}

	private void assertJsonPropertyEquals(VehicleDto vehicle, String key, String value) throws Exception {
		assertThat(json.write(vehicle)).extractingJsonPathStringValue(key).isEqualTo(value);
	}
}
