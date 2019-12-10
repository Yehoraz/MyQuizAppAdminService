package com.MyQuizAppAdminService.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminServerException extends Exception {

	private Object item;
	private String methodThrown;

	public AdminServerException(Object item, String methodThrown, String message) {
		super(message);
		setItem(item);
		setMethodThrown(methodThrown);
	}

}
