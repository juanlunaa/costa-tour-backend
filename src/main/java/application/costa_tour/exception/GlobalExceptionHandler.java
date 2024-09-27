package application.costa_tour.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handlerUnauthorizedException(
            UnauthorizedException ex,
            WebRequest webRequest
    ) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        ex.getMessage(),
                        webRequest.getDescription(false)),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AdminAlreadyExistException.class)
    public ResponseEntity<?> handlerAdminAlreadyExistException (
            AdminAlreadyExistException ex,
            WebRequest webRequest
    ) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        ex.getMessage(),
                        webRequest.getDescription(false)),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<?> handlerInvalidCredentialsException (
            InvalidCredentialsException ex,
            WebRequest webRequest
    ) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        ex.getMessage(),
                        webRequest.getDescription(false)),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handlerResourceNotFoundException (
            ResourceNotFoundException ex,
            WebRequest webRequest
    ) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        ex.getMessage(),
                        webRequest.getDescription(false)),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handlerBadRequestException (
            BadRequestException ex,
            WebRequest webRequest
    ) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        ex.getMessage(),
                        webRequest.getDescription(false)),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handlerMethodArgumentNotValidException (
            MethodArgumentNotValidException ex,
            WebRequest webRequest
    ) {
        Map<String, String> mapErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(err -> {
            String key = ((FieldError) err).getField();
            String value = err.getDefaultMessage();

            mapErrors.put(key, value);
        });

        return new ResponseEntity<>(
                new ErrorResponse(
                        mapErrors.toString(),
                        webRequest.getDescription(false)),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClientAlredyExistException.class)
    public ResponseEntity<?> handlerClientAlreadyExistException (
            ClientAlredyExistException ex,
            WebRequest webRequest
    ) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        ex.getMessage(),
                        webRequest.getDescription(false)),
                HttpStatus.CONFLICT);
    }
}
