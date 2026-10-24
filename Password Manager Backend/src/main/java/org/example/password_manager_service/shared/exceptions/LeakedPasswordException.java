package org.example.password_manager_service.shared.exceptions;

public class LeakedPasswordException extends RuntimeException{
    public LeakedPasswordException(String message){
        super(message);
    }
}
