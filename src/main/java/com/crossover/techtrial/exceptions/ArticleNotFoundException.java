package com.crossover.techtrial.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ArticleNotFoundException extends Exception {
	
	
	private static final long serialVersionUID = 9082566445629034753L;

	public ArticleNotFoundException(Long id) {
		super(String.format("Article %d is not found.", id));
	}
}
