package com.home.fileserver.exception;

public class AuthorizationException extends RuntimeException {

    private String clientId;

    public AuthorizationException(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }
}
