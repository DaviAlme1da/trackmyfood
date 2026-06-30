package br.com.davi.trackmyfood.core.exceptions;

public class RouteCalculationException extends AppException  {
    public RouteCalculationException(String message) {
        super(message);
    }

    public RouteCalculationException() {
        super("Error calculating route");
    }
}
