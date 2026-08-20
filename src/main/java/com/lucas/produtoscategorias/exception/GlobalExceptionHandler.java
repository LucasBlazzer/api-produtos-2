package com.lucas.produtoscategorias.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ApiError> tratarNaoEncontrado(
            RecursoNaoEncontradoException erro,
            HttpServletRequest request
    ) {
        return resposta(HttpStatus.NOT_FOUND, erro.getMessage(), request);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ApiError> tratarRegraNegocio(
            RegraNegocioException erro,
            HttpServletRequest request
    ) {
        return resposta(HttpStatus.CONFLICT, erro.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> tratarValidacao(
            MethodArgumentNotValidException erro,
            HttpServletRequest request
    ) {
        String mensagem = erro.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(campo -> campo.getField() + ": " + campo.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return resposta(HttpStatus.BAD_REQUEST, mensagem, request);
    }

    private ResponseEntity<ApiError> resposta(
            HttpStatus status,
            String mensagem,
            HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(apiError);
    }
}
