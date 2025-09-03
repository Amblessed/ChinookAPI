package com.amblessed.chinookapi.exception;

public class OpenAIServiceException extends Exception {
    public OpenAIServiceException(String message) {
        super(message);
    }

    public OpenAIServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
