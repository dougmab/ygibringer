package com.github.dougmab.ygibringer.server.exception;

public class NoUserAssociatedException extends RuntimeException {
    public NoUserAssociatedException(String message) {
        super(message);
    }
}
