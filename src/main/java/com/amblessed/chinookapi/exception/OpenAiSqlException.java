package com.amblessed.chinookapi.exception;



/*
 * @Project Name: ChinookAPI
 * @Author: Okechukwu Bright Onwumere
 * @Created: 03-Sep-25
 */


public class OpenAiSqlException extends RuntimeException {
    public OpenAiSqlException(String message) {
        super(message);
    }

    public OpenAiSqlException(String message, Throwable cause) {
        super(message, cause);
    }
}
