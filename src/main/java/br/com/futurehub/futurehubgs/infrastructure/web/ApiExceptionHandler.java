package br.com.futurehub.futurehubgs.infrastructure.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArg(IllegalArgumentException ex, WebRequest req) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body(HttpStatus.BAD_REQUEST, ex.getMessage(), req));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElement(NoSuchElementException ex, WebRequest req) {
        String msg = ex.getMessage() != null
                ? ex.getMessage()
                : i18n("error.bad_request", "Requisição inválida");

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(body(HttpStatus.NOT_FOUND, msg, req));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex, WebRequest req) {
        var details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        f -> f.getField(),
                        f -> f.getDefaultMessage(),
                        (a, b) -> a
                ));

        String msg = i18n("error.validation", "Validation error");

        return ResponseEntity
                .badRequest()
                .body(body(HttpStatus.BAD_REQUEST, msg, req, details));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex, WebRequest req) {
        // Fallback genérico para evitar vazar stacktrace/erro feio
        String msg = i18n("error.internal", "Internal server error");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body(HttpStatus.INTERNAL_SERVER_ERROR, msg, req, null));
    }

    // ===== Helpers =====

    private Map<String, Object> body(HttpStatus status, String msg, WebRequest req) {
        return body(status, msg, req, null);
    }

    private Map<String, Object> body(HttpStatus status, String msg, WebRequest req, Object details) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", Instant.now().toString());
        map.put("status", status.value());
        map.put("error", status.getReasonPhrase());
        map.put("message", msg);
        map.put("path", req.getDescription(false).replace("uri=", ""));
        if (details != null) {
            map.put("details", details);
        }
        return map;
    }

    private String i18n(String code, String defaultMessage) {
        var locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, null, defaultMessage, locale);
    }
}


