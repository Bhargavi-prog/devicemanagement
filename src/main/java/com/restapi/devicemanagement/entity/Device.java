package com.restapi.devicemanagement.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Device")
public class Device {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank(message = "DeviceID shouldn't be null")
	private String deviceId;

	@NotBlank(message = "RecordType shouldn't be null")
	private String recordType;

	@NotBlank(message = "EventDateTime shouldn't be null")
	private String eventDateTime;

	@Min(10)
	@Max(5000)
	private int fieldA;

	@NotBlank(message = "FieldB shouldn't be null")
	private String fieldB;

	@DecimalMax("10000.0")
	@DecimalMin("0.0")
	private double fieldC;

	public Device(@NotBlank(message = "DeviceID shouldn't be null") String deviceId,
			@NotBlank(message = "RecordType shouldn't be null") String recordType,
			@NotBlank(message = "EventDateTime shouldn't be null") String eventDateTime, @Min(10) @Max(5000) int fieldA,
			@NotBlank(message = "FieldB shouldn't be null") String fieldB,
			@DecimalMax("10000.0") @DecimalMin("0.0") double fieldC) {
		super();
		this.deviceId = deviceId;
		this.recordType = recordType;
		this.eventDateTime = eventDateTime;
		this.fieldA = fieldA;
		this.fieldB = fieldB;
		this.fieldC = fieldC;
	}

}