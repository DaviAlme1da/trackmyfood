package br.com.davi.trackmyfood.core.exceptions;

public class BusinessException extends AppException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException() {
        super("Required delivery for this status");
    }
}
