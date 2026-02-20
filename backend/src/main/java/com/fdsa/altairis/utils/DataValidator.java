package com.fdsa.altairis.utils;

import com.fdsa.altairis.exception.ApplicationException;

public final class DataValidator {
    private DataValidator(){

    }

    public static void requiere(boolean condition, String code, String message){
        if(!condition){
            throw new ApplicationException(code,message);
        }
    }
}
