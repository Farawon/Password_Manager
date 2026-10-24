package org.example.password_manager_service.shared.exceptions;

public class IncorrectJwtToken extends RuntimeException{
    public IncorrectJwtToken(){super("This token wrong. Try later" );}
}
