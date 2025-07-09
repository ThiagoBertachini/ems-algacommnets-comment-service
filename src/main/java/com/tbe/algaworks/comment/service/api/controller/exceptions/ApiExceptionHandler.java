package com.tbe.algaworks.comment.service.api.controller.exceptions;

import com.tbe.algaworks.comment.service.api.client.exceptions.ModerationClientBadGatewayException;
import com.tbe.algaworks.comment.service.api.client.exceptions.UnprocessableEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.channels.ClosedChannelException;
import java.time.OffsetDateTime;
import java.util.UUID;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UnprocessableEntityException.class)
    public ProblemDetail handleUnprocessableEntityException(UnprocessableEntityException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler({
            SocketTimeoutException.class,
            ConnectException.class,
            ClosedChannelException.class
    })
    public ProblemDetail handleConnectionException(IOException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.GATEWAY_TIMEOUT);

        problemDetail.setTitle("Gateway Timeout");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("https://httpstatuses.com/504"));

        return problemDetail;
    }

    @ExceptionHandler(ModerationClientBadGatewayException.class)
    public ProblemDetail handleModerationClientBadGatewayException(ModerationClientBadGatewayException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_GATEWAY);

        problemDetail.setTitle("Bad gateway");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("https://httpstatuses.com/502"));

        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setProperty("timestamp", OffsetDateTime.now());
        problemDetail.setInstance(URI.create("/usuarios/" + ex.getValue()));

        if (ex.getRequiredType() == UUID.class) {
            problemDetail.setTitle("UUID inválido");
            problemDetail.setDetail("O identificador fornecido não é um UUID válido.");
        } else {
            problemDetail.setTitle("Parâmetro inválido");
            problemDetail.setDetail("Erro ao processar o parâmetro: " + ex.getName());
        }

        return problemDetail;
    }

}
