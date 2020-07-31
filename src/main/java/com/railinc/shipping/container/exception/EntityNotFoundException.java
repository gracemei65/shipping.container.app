package com.railinc.shipping.container.exception;


public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
