package application.costa_tour.dto.plan;

import application.costa_tour.model.enums.PlanCategory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PlanExclusivoCreateDTO {
    @NotEmpty
    private String nombre;
    @NotEmpty
    private String descripcion;
    @NotNull
    private PlanCategory categoria;
    @NotEmpty
    private String precio;
    private List<MultipartFile> imagenesFiles;
    @NotNull
    private float latitud;
    @NotNull
    private float longitud;
    @NotEmpty
    private String direccion;
    @NotEmpty
    private String notaUbicacion;
    @NotEmpty
    private List<String> serviciosIncluidos;
    @NotEmpty
    private String expectativa;
    @NotEmpty
    private List<String> informacionAdicional;
    @NotEmpty
    private List<DisponibilidadDTO> disponibilidad;
    @NotNull
    private int miniaturaSelect;
    @NotEmpty
    private List<Long> caracteristicas;
}
