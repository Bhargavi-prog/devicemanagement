package com.restapi.devicemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.restapi.devicemanagement.entity.Device;

public interface DeviceRepository extends JpaRepository<Device, String> {

}
