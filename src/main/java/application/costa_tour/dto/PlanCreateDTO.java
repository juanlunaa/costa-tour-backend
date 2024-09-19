package application.costa_tour.dto;

import application.costa_tour.model.enums.PlanCategory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PlanCreateDTO {
    @NotEmpty
    private String nombre;
    @NotEmpty
    private String descripcion;
    @NotNull
    private PlanCategory categoria;
    @NotEmpty
    private String rangoMinDinero;
    @NotEmpty
    private String rangoMaxDinero;
    @NotNull
    private List<MultipartFile> imagenesFiles;
    @NotNull
    private float latitud;
    @NotNull
    private float longitud;
    @NotEmpty
    private String direccion;
    @NotNull
    private int miniaturaSelect;
    @NotEmpty
    private List<Long> caracteristicas;
}
