package application.costa_tour.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ClienteCreateDTO {
    @NotEmpty
    private String dni;
    @NotNull
    private Long idTipoDocumento;
    @NotEmpty
    private String nombre;
    @NotEmpty
    private String apellido;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaNacimiento;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String telefono;
    @NotNull
    private Long idCiudad;
}
