package br.com.davi.trackmyfood.api.common.handler;

import br.com.davi.trackmyfood.api.common.dtos.ErrorResponse;
import br.com.davi.trackmyfood.core.exceptions.BusinessException;
import br.com.davi.trackmyfood.core.exceptions.DeliveryManNotFoundException;
import br.com.davi.trackmyfood.core.exceptions.NotFoundException;
import br.com.davi.trackmyfood.core.exceptions.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(
            NotFoundException ex){

        var erro = ErrorResponse.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException (
            BusinessException  ex){

        var erro = ErrorResponse.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(erro);
    }

    @ExceptionHandler(Exception .class)
    public ResponseEntity<ErrorResponse> handleException (
            Exception   ex){

        var erro = ErrorResponse.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }


}

