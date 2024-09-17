package application.costa_tour.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AliadoCreateDTO {
    @NotEmpty
    private String nitAliado;
    @NotEmpty
    private String nombreEmpresa;
    @NotEmpty
    private String direccion;
    @NotEmpty
    private String telefono;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String password;
}
