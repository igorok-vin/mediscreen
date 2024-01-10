package com.mediscreen.patientservice.utils;

import com.mediscreen.patientservice.exception.BirthdateException;
import com.mediscreen.patientservice.exception.PatientNotFoundException;
import com.mediscreen.patientservice.model.AppError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<AppError> noteNotFoundException(BirthdateException exception) {
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> patientNotFoundException(PatientNotFoundException exception) {
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), exception.getMessage()), HttpStatus.NOT_FOUND);
    }

}
