package br.com.davi.trackmyfood.core.exceptions;

public class OrderNotFoundException extends  NotFoundException{

    public OrderNotFoundException(String message) {
        super(message);
    }
    public OrderNotFoundException() {
        super("order not found");
    }

}
