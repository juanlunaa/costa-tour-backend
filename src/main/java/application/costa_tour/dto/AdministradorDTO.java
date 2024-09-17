package application.costa_tour.dto;

import application.costa_tour.model.enums.UserRole;
import lombok.Data;

@Data
public class AdministradorDTO {
    private Long userId;
    private UserRole tipoUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String avatar;
}
