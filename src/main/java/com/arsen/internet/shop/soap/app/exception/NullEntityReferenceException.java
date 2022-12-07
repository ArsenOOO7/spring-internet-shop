package com.arsen.internet.shop.soap.app.exception;


/**
 * Called when entity == null
 * @author Arsen Sydoryk
 */
public class NullEntityReferenceException extends RuntimeException{
    public NullEntityReferenceException(String message) {
        super(message);
    }
}
