package application.costa_tour.dto;

import lombok.Data;

@Data
public class CodigoPlanDTO {
    private Long id;
    private String codigo;
    private TuristaDTO turista;
    private PlanDTO plan;
    private String fechaGeneracion;
    private String fechaUso;
    private String estado;
}
