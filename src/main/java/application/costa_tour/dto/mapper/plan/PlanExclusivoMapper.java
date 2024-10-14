package application.costa_tour.dto.mapper.plan;

import application.costa_tour.dto.CaracteristicaPlanDTO;
import application.costa_tour.dto.UbicacionDTO;
import application.costa_tour.dto.plan.DisponibilidadDTO;
import application.costa_tour.dto.plan.PlanExclusivoDTO;
import application.costa_tour.model.DisponibilidadPlanExclusivo;
import application.costa_tour.model.PlanExclusivo;
import application.costa_tour.model.enums.DayWeek;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Mapper
public interface PlanExclusivoMapper {

    PlanExclusivoMapper mapper = Mappers.getMapper(PlanExclusivoMapper.class);

    List<PlanExclusivoDTO> planExclusivosToPlanExclusivoDtos (List<PlanExclusivo> planesExclusivos);

    default PlanExclusivoDTO planExclusivoToPlanExclusivoDto (PlanExclusivo planExclusivo) {
        if (planExclusivo == null) {
            return null;
        }

        PlanExclusivoDTO planExclusivoDto = new PlanExclusivoDTO();

        planExclusivoDto.setId(planExclusivo.getId());
        planExclusivoDto.setNombre(planExclusivo.getNombre());
        planExclusivoDto.setDescripcion(planExclusivo.getDescripcion());
        planExclusivoDto.setCategoria(planExclusivo.getCategoria());
        planExclusivoDto.setPrecio(planExclusivo.getRangoMaxDinero());
        planExclusivoDto.setMiniatura(planExclusivo.getImagenMiniatura());
        planExclusivoDto.setExpectativa(planExclusivo.getExpectativa());
        planExclusivoDto.setNotaUbicacion(planExclusivo.getNotaUbicacion());

        planExclusivoDto.setImagenes(
                planExclusivo.getImagenes()
                        .stream()
                        .map(i -> {
                            return i.getUrl();
                        })
                        .collect(Collectors.toList())
        );

        planExclusivoDto.setCaracteristicas(
                planExclusivo.getCaracteristicasPlan()
                        .stream()
                        .map(c -> {
                            return new CaracteristicaPlanDTO(c.getCaracteristica().getId(), c.getCaracteristica().getPalabraClave());
                        })
                        .collect(Collectors.toList()));

        UbicacionDTO ubicacionDto = new UbicacionDTO(
                planExclusivo.getUbicacion().getLatitud(),
                planExclusivo.getUbicacion().getLongitud(),
                planExclusivo.getUbicacion().getDireccion()
        );

        planExclusivoDto.setUbicacion(ubicacionDto);

        planExclusivoDto.setServiciosIncluidos(
                planExclusivo.getServiciosIncluidos()
                        .stream()
                        .map(s -> {
                            return s.getServicio();
                        })
                        .collect(Collectors.toList())
        );

        planExclusivoDto.setInformacionAdicional(
                planExclusivo.getInformacionAdicional()
                        .stream()
                        .map(i -> {
                            return i.getDescripcion();
                        })
                        .collect(Collectors.toList())
        );


        Map<DayWeek, List<LocalTime>> disponibilidadMap = new HashMap<>();

        for (DisponibilidadPlanExclusivo disponibilidad : planExclusivo.getDisponibilidad()) {
            if (!disponibilidadMap.containsKey(disponibilidad.getDiaSemana())) {
                disponibilidadMap.put(disponibilidad.getDiaSemana(), new ArrayList<>(Arrays.asList(disponibilidad.getHora())));
            } else {
                disponibilidadMap.get(disponibilidad.getDiaSemana()).add(disponibilidad.getHora());
            }
        }

        planExclusivoDto.setDisponibilidad(
                disponibilidadMap.entrySet()
                        .stream()
                        .map(e -> {
                            return new DisponibilidadDTO(e.getKey(), e.getValue());
                        })
                        .collect(Collectors.toList())
        );

        return planExclusivoDto;
    }

}
