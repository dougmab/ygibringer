package com.github.dougmab.ygibringer.server;

import com.github.dougmab.ygibringer.server.exception.CustomBlockDisabledException;
import com.github.dougmab.ygibringer.server.exception.EndOfListException;
import com.github.dougmab.ygibringer.server.model.ErrorInfoData;
import com.github.dougmab.ygibringer.server.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIoException(IOException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(new ErrorInfoData("fail_file_handling", e.getLocalizedMessage())));
    }

    @ExceptionHandler(EndOfListException.class)
    public ResponseEntity<ErrorResponse> handleEndOfListException(EndOfListException e) {
        System.out.println(e.getLocalizedMessage());

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CustomBlockDisabledException.class)
    public ResponseEntity<ErrorResponse> handleCustomBlockDisabledException(CustomBlockDisabledException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErrorResponse(new ErrorInfoData("custom_block_disabled", e.getLocalizedMessage())));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(new ErrorInfoData("unpredicted_error", e.getLocalizedMessage())));
    }

}
