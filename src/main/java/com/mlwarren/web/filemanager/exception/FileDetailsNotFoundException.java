package com.mlwarren.web.filemanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FileDetailsNotFoundException extends RuntimeException {
    public FileDetailsNotFoundException(String exception) {
        super(exception);
    }
}
