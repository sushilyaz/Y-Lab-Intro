package org.example.exception;

/**
 * эксепшен на проверку авторизации
 */
public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message) {
        super(message);
    }
}
