package com.home.assignment.urlshortener.error;

import com.home.assignment.urlshortener.controller.ErrorController;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class UrlShortenerExceptionHandler {

    private static final String ERROR_CODE_NUMBER = "errorCode";
    private static final String ARGUMENTS = "arguments";

    @ExceptionHandler(UrlShortenerException.class)
    protected ResponseEntity<ProblemDetail> handleUrlShortenerException(UrlShortenerException ex, WebRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(ex.getErrorCode().getHttpStatus());
        problemDetail.setType(URI.create(ErrorController.PATH + "/" + ex.getErrorCode().getNumber()));
        problemDetail.setTitle(ex.getMessage());
        problemDetail.setProperty(ERROR_CODE_NUMBER, ex.getErrorCode().getNumber());
        problemDetail.setTitle(ex.getErrorCode().getTitle());
        problemDetail.setDetail(ex.getErrorCode().getDescription());

        if (ex.getArguments() != null && !ex.getArguments().isEmpty()) {
            problemDetail.setProperty(ARGUMENTS, ex.getArguments());
        }

        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ProblemDetail> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = ex.getBindingResult().getAllErrors().stream()
                .collect(Collectors.toMap(
                        error -> (error instanceof FieldError) ? ((FieldError) error).getField() : error.getObjectName(),
                        ObjectError::getDefaultMessage,
                        (existing, replacement) -> existing
                ));
        return handleUrlShortenerException(new UrlShortenerException(ErrorCode.INVALID_REQUEST, errors), request);
    }

}
