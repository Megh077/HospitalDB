package com.example.Hospital.advice;

import com.example.Hospital.Utils.Constants;
import com.example.Hospital.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDTO> NotFoundException(NotFoundException ex){
        return new ResponseEntity<>(ResponseDTO.builder().message(ex.getMessage()).data(null).build(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ResponseDTO> InvalidInputException(InvalidInputException ex){
        return new ResponseEntity<>(ResponseDTO.builder().message(ex.getMessage()).data(null).build(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResponseDTO> ValidationException(ValidationException ex){
        return new ResponseEntity<>(ResponseDTO.builder().message(ex.getMessage()).data(null).build(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleGeneralException(Exception ex){
    return new ResponseEntity<>(ResponseDTO.builder().message(Constants.UNEXPECTED_ERROR).data(null).build(), HttpStatus.BAD_REQUEST);
    }

}
