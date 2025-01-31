package com.softserve.exception.apierror;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class ApiValidationError implements ApiSubError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    ApiValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }
}

