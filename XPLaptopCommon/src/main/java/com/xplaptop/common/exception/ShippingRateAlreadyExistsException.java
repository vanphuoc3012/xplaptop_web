package com.xplaptop.common.exception;

import lombok.NonNull;

public class ShippingRateAlreadyExistsException extends Throwable {
    public ShippingRateAlreadyExistsException(@NonNull String message) {
        super(message);
    }
}
