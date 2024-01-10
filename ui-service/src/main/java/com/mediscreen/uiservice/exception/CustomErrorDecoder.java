package com.mediscreen.uiservice.exception;


import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {
        if (response.status() == 404) {
            return new PatientNotFoundException("Patient with such a last name not found");
        } else if (response.status() == 500) {
            return new BirthdateException("Invalid date of birth");
        }
        return defaultErrorDecoder.decode(s, response);
    }
}
