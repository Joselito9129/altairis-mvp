package com.fdsa.altairis.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class GenericResponse<T> {

    private boolean success;
    private String message;
    private String code;
    private T data;

    public static <T> GenericResponse<T> ok(T data, String message) {
        return GenericResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> GenericResponse<T> fail(String code, String message) {
        return GenericResponse.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .build();
    }

}
