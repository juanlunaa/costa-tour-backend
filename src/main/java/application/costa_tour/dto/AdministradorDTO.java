package application.costa_tour.dto;

import application.costa_tour.model.AdminRole;
import application.costa_tour.model.UserRole;
import lombok.Data;

@Data
public class AdministradorDTO {
    private Long userId;
    private UserRole tipoUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String avatar;
    private AdminRole rol;
}
