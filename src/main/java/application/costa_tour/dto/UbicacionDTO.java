package application.costa_tour.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UbicacionDTO {
//    private Long id;
    private float latitud;
    private float longitud;
    private String direccion;
}
