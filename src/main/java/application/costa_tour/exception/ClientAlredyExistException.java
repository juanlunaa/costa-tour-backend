package application.costa_tour.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ClientAlredyExistException extends RuntimeException {

    public ClientAlredyExistException(String message) {
        super(message);
    }

}
