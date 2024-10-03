package application.costa_tour.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class TuristaUpdateDTO {
    @NotEmpty
    private String dni;
    @NotEmpty
    private String nombre;
    @NotEmpty
    private String apellido;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaNacimiento;
    @NotNull
    private Long idCiudad;
    @NotEmpty
    private List<Long> intereses;
}
