package br.com.davi.trackmyfood.core.exceptions;

public class NotFoundException extends RuntimeException  {

    public NotFoundException(String message) {
        super(message);
    }

}
