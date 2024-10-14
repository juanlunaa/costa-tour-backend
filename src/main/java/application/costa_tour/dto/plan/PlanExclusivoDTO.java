package application.costa_tour.dto.plan;

import application.costa_tour.dto.CaracteristicaPlanDTO;
import application.costa_tour.dto.HechoPlanDTO;
import application.costa_tour.dto.UbicacionDTO;
import application.costa_tour.model.enums.PlanCategory;
import lombok.Data;

import java.util.List;

@Data
public class PlanExclusivoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private PlanCategory categoria;
    private String precio;
    private String miniatura;
    private List<String> imagenes;
    private List<CaracteristicaPlanDTO> caracteristicas;
    private UbicacionDTO ubicacion;
    private float calificacionPromedio;
    private String expectativa;
    private String notaUbicacion;
    private List<String> serviciosIncluidos;
    private List<String> informacionAdicional;
    private List<DisponibilidadDTO> disponibilidad;
}
