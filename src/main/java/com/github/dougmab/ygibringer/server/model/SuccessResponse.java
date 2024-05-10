package com.github.dougmab.ygibringer.server.model;

public record SuccessResponse<T>(boolean success, T data) {
    public SuccessResponse(T data) {
        this(true, data);
    }
}
