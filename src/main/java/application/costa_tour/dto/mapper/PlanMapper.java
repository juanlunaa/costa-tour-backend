package application.costa_tour.dto.mapper;

import application.costa_tour.dto.CaracteristicaPlanDTO;
import application.costa_tour.dto.HechoPlanDTO;
import application.costa_tour.dto.PlanDTO;
import application.costa_tour.dto.UbicacionDTO;
import application.costa_tour.model.CaracteristicaPlan;
import application.costa_tour.model.Plan;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
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
        planDto.setRangoMinDinero(plan.getRangoMinDinero());
        planDto.setRangoMaxDinero(plan.getRangoMaxDinero());
        planDto.setMiniatura(plan.getImagenMiniatura());

        planDto.setImagenes(
                plan.getImagenes()
                        .stream()
                        .map(i -> {
                            return i.getUrl();
                        })
                        .collect(Collectors.toList())
        );

        planDto.setHechos(
                plan.getHechos() != null ?
                        plan.getHechos()
                                .stream()
                                .map(h -> {
                                    return new HechoPlanDTO(h.getId(), h.getDecripcion());
                                })
                                .collect(Collectors.toList())
                        : new ArrayList<>()
        );

        planDto.setCaracteristicas(
                plan.getCaracteristicasPlan()
                        .stream()
                        .map(c -> {
                            return new CaracteristicaPlanDTO(c.getCaracteristica().getId(), c.getCaracteristica().getPalabraClave());
                        })
                        .collect(Collectors.toList()));


        UbicacionDTO ubicacionDto = new UbicacionDTO(
//                plan.getUbicacion().getId(),
                plan.getUbicacion().getLatitud(),
                plan.getUbicacion().getLongitud(),
                plan.getUbicacion().getDireccion()
        );

        planDto.setUbicacion(ubicacionDto);

        return planDto;
    }
}
