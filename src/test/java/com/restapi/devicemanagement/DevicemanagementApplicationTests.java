package com.restapi.devicemanagement;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restapi.devicemanagement.entity.AuthRequest;
import com.restapi.devicemanagement.entity.Device;
import com.restapi.devicemanagement.repository.DeviceRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DevicemanagementApplicationTests {
	@LocalServerPort
	private int port;

	private static RestTemplate restTemplate;

	private String token;

	private String baseUrl;

	@Autowired
	public DeviceRepository deviceRepository;

	HttpHeaders headers = new HttpHeaders();

	@BeforeAll
	public static void init() {
		restTemplate = new RestTemplate();
	}

	@BeforeEach
	public void getToken() {
		baseUrl = "http://localhost:" + port;
		AuthRequest auth = new AuthRequest();
		auth.setPassword("tulasi");
		auth.setUserName("tulasi");

		token = restTemplate.postForObject(createURLWithPort("/token"), auth, String.class);
	}

	@Test
	public void echoDeviceTest() {
		Device device = new Device("357370040159770", "echodevice", "2022-06-12T05:09:48Z", 57, "hearbeat", 189.5);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(HttpHeaders.AUTHORIZATION, token);
		HttpEntity<Device> entity = new HttpEntity<Device>(device, headers);
		Device devRes = restTemplate.postForObject(createURLWithPort("/echo"), entity, Device.class);
		assertEquals("357370040159770", devRes.getDeviceId());
		assertEquals(1, deviceRepository.findAll().size());
	}

	@Test
	public void getDeviceTest() {
		Device device = new Device("357370040159770", "deviceadd", "2022-07-20T05:09:48Z", 57, "hearbeat", 189.5);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(HttpHeaders.AUTHORIZATION, token);
		HttpEntity<Device> entity = new HttpEntity<Device>(device, headers);
		String deviceId = restTemplate.postForObject(createURLWithPort("/device"), entity, String.class);
		assertEquals("357370040159770", deviceId);
	}

	@Test
	public void noContentTest() {
		Device device = new Device();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(HttpHeaders.AUTHORIZATION, token);
		HttpEntity<Device> entity = new HttpEntity<Device>(device, headers);

		ResponseEntity<String> deviceId = restTemplate.exchange(createURLWithPort("/echo/nocontent"), HttpMethod.POST,
				entity, String.class);
		assertEquals(HttpStatus.NO_CONTENT, deviceId.getStatusCode());
	}

	@Test
	public void defaultCaseTest() {
		Device device = new Device();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(HttpHeaders.AUTHORIZATION, token);
		HttpEntity<Device> entity = new HttpEntity<Device>(device, headers);

		try {
			restTemplate.exchange(createURLWithPort("/device1"), HttpMethod.POST, entity, String.class);
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
		}
	}

	@Test
	public void validationTestForDeviceId() throws JsonMappingException, JsonProcessingException {
		Device device = new Device("", "deviceadd", "2022-07-20T05:09:48Z", 57, "hearbeat", 189.5);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(HttpHeaders.AUTHORIZATION, token);
		HttpEntity<Device> entity = new HttpEntity<Device>(device, headers);

		try {
			restTemplate.postForObject(createURLWithPort("/echo"), entity, Device.class);
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> map = mapper.readValue(e.getResponseBodyAsString(), Map.class);
			assertEquals("DeviceID shouldn't be null", map.get("deviceId"));
		}
	}

	@Test
	public void validationTestForFieldA() throws JsonMappingException, JsonProcessingException {
		Device device = new Device("357370040159770", "deviceadd", "2022-07-20T05:09:48Z", 0, "hearbeat", 189.5);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(HttpHeaders.AUTHORIZATION, token);
		HttpEntity<Device> entity = new HttpEntity<Device>(device, headers);

		try {
			restTemplate.postForObject(createURLWithPort("/echo"), entity, Device.class);
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> map = mapper.readValue(e.getResponseBodyAsString(), Map.class);
			assertEquals("must be greater than or equal to 10", map.get("fieldA"));
		}
	}

	public String createURLWithPort(String uri) {
		return baseUrl + uri;
	}

}
