package com.crossover.techtrial.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler({ ArticleNotFoundException.class })
	public ResponseEntity<?> handle(Exception exception) {
		LOG.error("ArticleNotFoundException: article not found! ", exception);

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), null);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

		LOG.error("MethodArgumentNotValidException: Validation fields error! Please check your request! ", ex);

		List<String> errors = new ArrayList<>();
		for (FieldError field : ex.getBindingResult().getFieldErrors()) {
			errors.add(String.format("%s: %s ", field.getObjectName(), field.getDefaultMessage()));
		}
		for (ObjectError object : ex.getBindingResult().getGlobalErrors()) {
			errors.add(String.format("%s: %s ", object.getObjectName(), object.getDefaultMessage()));
		}
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation fields error! Please check your request!", errors);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
}
