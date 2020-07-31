
package com.railinc.shipping.container.exception;

import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;

@Service
public class JmsExceptionHandler implements ErrorHandler {
    @Override
    public void handleError(Throwable t) {
        System.out.println("invalid message " + t.getMessage());
    }
}
