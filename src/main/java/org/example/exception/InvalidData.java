package org.example.exception;

/**
 * эксепшен для валидации
 */
public class InvalidData extends RuntimeException{
    public InvalidData(String message) {
        super(message);
    }
}
