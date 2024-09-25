package application.costa_tour.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResDTO<T> {
    private T user;
    private String token;
}
