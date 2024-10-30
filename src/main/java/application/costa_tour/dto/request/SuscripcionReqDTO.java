package application.costa_tour.dto.request;

import lombok.Data;

@Data
public class SuscripcionReqDTO {
    private String successUrl;
    private String failureUrl;
}
