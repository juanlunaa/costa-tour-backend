package application.costa_tour.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AdministradorUpdateDTO {
    @NotEmpty
    private String nombre;
    @NotEmpty
    private String apellido;
}
