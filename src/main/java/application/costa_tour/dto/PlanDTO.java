package application.costa_tour.dto;

import application.costa_tour.model.CaracteristicaPlan;
import application.costa_tour.model.enums.PlanCategory;
import lombok.Data;

import java.util.List;

@Data
public class PlanDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private PlanCategory categoria;
    private String rangoMinDinero;
    private String rangoMaxDinero;
    private String miniatura;
    private List<String> imagenes;
    private List<HechoPlanDTO> hechos;
    private List<CaracteristicaPlanDTO> caracteristicas;
    private UbicacionDTO ubicacion;
    private float calificacionPromedio;
}
