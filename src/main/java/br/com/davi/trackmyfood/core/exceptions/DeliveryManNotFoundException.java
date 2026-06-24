package br.com.davi.trackmyfood.core.exceptions;

public class DeliveryManNotFoundException extends NotFoundException {

    public DeliveryManNotFoundException(String message) {
        super(message);
    }
    public DeliveryManNotFoundException() {
        super("deliveryMan not found");
    }
}
