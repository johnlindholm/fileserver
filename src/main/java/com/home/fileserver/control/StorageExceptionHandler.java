package com.home.fileserver.control;

import com.home.fileserver.exception.AuthorizationException;
import com.home.fileserver.exception.StorageDataNotFoundException;
import com.home.fileserver.exception.USBStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class StorageExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(StorageExceptionHandler.class);

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<String> accessDenied(final AuthorizationException e) {
        logger.warn("Access denied for id: " + e.getClientId(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access is denied for ClientID " + e.getClientId());
    }

    @ExceptionHandler(StorageDataNotFoundException.class)
    public ResponseEntity<String> dataNotFound(final StorageDataNotFoundException e) {
        logger.warn("Data not found for id: " + e.getDataId(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data with id " + e.getDataId() + " was not found");
    }

    @ExceptionHandler(USBStorageException.class)
    public ResponseEntity<String> usbStorageException(final USBStorageException e) {
        logger.warn("USB storage exception", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> other(final Throwable e) {
        logger.error("Unknown exception thrown", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
