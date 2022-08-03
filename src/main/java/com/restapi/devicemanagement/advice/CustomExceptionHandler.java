package com.restapi.devicemanagement.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleInvalidInputs(MethodArgumentNotValidException ex) {

		Map<String, String> erroMap = new HashMap<String, String>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			erroMap.put(error.getField(), error.getDefaultMessage());
		});
		return erroMap;
	}
}
