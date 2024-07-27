package application.costa_tour.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ClienteCreateDTO {
    private String dni;
    private Long idTipoDocumento;
    private String tipoUsuario;
    private String nombre;
    private String apellido;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaNacimiento;
    private String email;
    private String password;
    private String telefono;
    private Long idCiudad;
}
