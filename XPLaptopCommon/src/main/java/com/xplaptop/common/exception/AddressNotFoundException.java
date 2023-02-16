package com.xplaptop.common.exception;

import java.util.NoSuchElementException;

public class AddressNotFoundException extends NoSuchElementException {
    public AddressNotFoundException(String addressNotFound) {
        super(addressNotFound);
    }
}
