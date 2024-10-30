package application.costa_tour.dto;

import application.costa_tour.model.enums.UserRole;
import lombok.Data;

@Data
public class AliadoDTO {
    private Long userId;
    private UserRole tipoUsuario;
    private String nitAliado;
    private String nombreEmpresa;
    private String direccion;
    private String telefono;
    private String email;
    private String avatar;
    private boolean exclusivo;
}
