package com.restapi.devicemanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restapi.devicemanagement.entity.Device;
import com.restapi.devicemanagement.repository.DeviceRepository;

@Service
public class DeviceService {
	@Autowired
	public DeviceRepository deviceRepository;

	public Device save(Device device) {
		return deviceRepository.save(device);
	}
}
