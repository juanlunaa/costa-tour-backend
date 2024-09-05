package application.costa_tour.dto;

import lombok.Data;

@Data
public class UbicacionDTO {
    private Long id;
    private float latitud;
    private float longitud;
    private String direccion;
}
