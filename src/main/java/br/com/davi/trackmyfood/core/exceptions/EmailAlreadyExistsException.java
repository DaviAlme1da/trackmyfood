package br.com.davi.trackmyfood.core.exceptions;

public class EmailAlreadyExistsException extends AppException{
    public EmailAlreadyExistsException(String message) {
        super(message);
    }

    public EmailAlreadyExistsException() {
        super("Email already in use.");
    }
}
