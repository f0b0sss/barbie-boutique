package com.barbieboutique.exceptions;

public class WrongOldPasswordException extends Exception {

    public WrongOldPasswordException(String message) {
        super(message);
    }
}
