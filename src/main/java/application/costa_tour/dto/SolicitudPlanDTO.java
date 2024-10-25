package application.costa_tour.dto;

import application.costa_tour.model.enums.RequestStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SolicitudPlanDTO {
    private Long id;
    private AliadoDTO aliado;
    private AdministradorDTO administrador;
    private PlanDTO plan;
    private RequestStatus estado;
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaRevision;
    private String comentarioRevision;
}
