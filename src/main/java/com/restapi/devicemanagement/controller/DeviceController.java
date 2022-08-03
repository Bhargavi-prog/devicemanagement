package com.restapi.devicemanagement.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.devicemanagement.entity.AuthRequest;
import com.restapi.devicemanagement.entity.Device;
import com.restapi.devicemanagement.security.AutheticationFilter;
import com.restapi.devicemanagement.service.DeviceService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class DeviceController {

	@Autowired
	public DeviceService deviceService;

	@Autowired
	public AutheticationFilter autheticationFilter;

	@PostMapping("/token")
	public String generateToken(@RequestBody AuthRequest authReq) throws Exception {
		log.info("Request received to generate token for user {}", authReq.getUserName());
		return autheticationFilter.createToken(authReq.getUserName());
	}

	@PostMapping("/echo")
	public ResponseEntity<Device> echoDevice(@RequestBody @Valid Device devicePayload) {
		log.info("Request payload in echo URL is {}", devicePayload);
		deviceService.save(devicePayload);
		return new ResponseEntity<>(devicePayload, HttpStatus.OK);
	}

	@PostMapping("/device")
	public ResponseEntity<String> getDevice(@RequestBody Device devicePayload) {
		log.info("Request payload in device URL is {}", devicePayload);
		deviceService.save(devicePayload);
		return new ResponseEntity<>(devicePayload.getDeviceId(), HttpStatus.OK);
	}

	@PostMapping("**")
	public ResponseEntity<String> notHandled() {
		log.warn("Request received for Bad Request URL");
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("*/nocontent")
	public ResponseEntity<String> noContent() {
		log.warn("Request received for no content URL");
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
