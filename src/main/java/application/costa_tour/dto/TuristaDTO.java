package application.costa_tour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TuristaDTO {
    private Long userId;
    private String dni;
    private String tipoUsuario;
    private String nombre;
    private String apellido;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fechaNacimiento;
    private Integer edad;
    private String email;
    private String avatar;
    private CiudadDTO ciudad;
    private EstadoDTO estado;
    private PaisDTO pais;
    private List<InteresTuristaDTO> intereses;
}
