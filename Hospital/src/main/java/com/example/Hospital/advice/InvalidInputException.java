package com.example.Hospital.advice;

public class InvalidInputException extends RuntimeException{
    public InvalidInputException (String message) {
        super(message);
    }
}
