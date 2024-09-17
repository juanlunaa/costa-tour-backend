package application.costa_tour.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TuristaDTO {
    private Long userId;
    private String dni;
    private String tipoUsuario;
    private String nombre;
    private String apellido;
    private Date fechaNacimiento;
    private Integer edad;
    private String email;
    private String avatar;
    private CiudadDTO ciudad;
    private EstadoDTO estado;
    private PaisDTO pais;
}
