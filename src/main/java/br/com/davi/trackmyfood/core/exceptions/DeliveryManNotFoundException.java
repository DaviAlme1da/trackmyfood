package br.com.davi.trackmyfood.core.exceptions;

public class DeliveryManNotFoundException extends AppException {


    public DeliveryManNotFoundException(String message) {
        super(message);
    }
    public DeliveryManNotFoundException() {
        super("deliveryMan not found");
    }
}
