package application.costa_tour.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AuthReqDTO {
    @NotEmpty
    private String email;
    @Length(min = 8, max = 16)
    @NotEmpty
    private String password;
}
