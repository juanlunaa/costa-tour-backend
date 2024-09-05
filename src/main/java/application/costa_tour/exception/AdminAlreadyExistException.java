package application.costa_tour.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class AdminAlreadyExistException extends RuntimeException {

    public AdminAlreadyExistException(String message) {
        super(message);
    }

}
