package application.costa_tour.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlanDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String categoria;
    private String rangoPrecio;
    private String miniatura;
    private List<String> imagenes;
    private UbicacionDTO ubicacion;
}
