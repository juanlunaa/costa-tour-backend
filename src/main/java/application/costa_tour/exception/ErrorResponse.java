package application.costa_tour.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ErrorResponse {
    private Date time = new Date();
    private String message;
    private String path;

    public ErrorResponse (String message, String path) {
        this.message = message;
        this.path = path.replace("uri=", "");
    }
}
