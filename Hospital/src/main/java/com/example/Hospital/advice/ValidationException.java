package com.example.Hospital.advice;

public class ValidationException extends RuntimeException{
    public ValidationException(String message){
        super(message);
    }
}
