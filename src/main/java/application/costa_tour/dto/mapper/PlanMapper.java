package application.costa_tour.dto.mapper;

import application.costa_tour.dto.PlanDTO;
import application.costa_tour.dto.UbicacionDTO;
import application.costa_tour.model.Plan;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mapper
public interface PlanMapper {

    PlanMapper mapper = Mappers.getMapper(PlanMapper.class);

    List<PlanDTO> plansToPlanDtos (List<Plan> planes);

    default PlanDTO planToPlanDto (Plan plan){
        if (plan == null) {
            return null;
        }

        PlanDTO planDto = new PlanDTO();

        planDto.setId(plan.getId());
        planDto.setNombre(plan.getNombre());
        planDto.setDescripcion(plan.getDescripcion());
        planDto.setCategoria(plan.getCategoria());
        planDto.setRangoPrecio(plan.getRangoPrecio());
        planDto.setMiniatura(plan.getImagenCard());

        List<String> imagenes = new ArrayList<>();

        //Separo las imagenes que estan juntas en un solo String para añadirlas a un Array

        Arrays
            .asList(plan.getImagenes().split(";"))
            .forEach(i -> {
                imagenes.add(i);
            });

        planDto.setImagenes(imagenes);

        UbicacionDTO ubicacionDto = new UbicacionDTO();

        ubicacionDto.setId(plan.getUbicacion().getId());
        ubicacionDto.setLatitud(plan.getUbicacion().getLatitud());
        ubicacionDto.setLongitud(plan.getUbicacion().getLongitud());
        ubicacionDto.setDireccion(plan.getUbicacion().getDireccion());

        planDto.setUbicacion(ubicacionDto);

        return planDto;
    }
}
