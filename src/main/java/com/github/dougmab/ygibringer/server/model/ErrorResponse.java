package com.github.dougmab.ygibringer.server.model;

public record ErrorResponse(boolean success, ErrorInfoData error) {
    public ErrorResponse(ErrorInfoData errorInfo) {
        this(false, errorInfo);
    }
}
