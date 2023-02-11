package com.xplaptop.common.exception;

import java.util.NoSuchElementException;

public class BrandNotFoundException extends NoSuchElementException {
	public BrandNotFoundException(String message) {
		super(message);
	}
}
