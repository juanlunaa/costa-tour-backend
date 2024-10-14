package application.costa_tour.dto.mapper.plan;

import application.costa_tour.dto.plan.PlanExclusivoCreateDTO;
import application.costa_tour.model.PlanExclusivo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlanExclusivoCreateMapper {

    PlanExclusivoCreateMapper mapper = Mappers.getMapper(PlanExclusivoCreateMapper.class);

    @Mapping(source = "latitud", target = "ubicacion.latitud")
    @Mapping(source = "longitud", target = "ubicacion.longitud")
    @Mapping(source = "direccion", target = "ubicacion.direccion")
    @Mapping(source = "precio", target = "rangoMaxDinero")
    @Mapping(target = "serviciosIncluidos", ignore = true)
    @Mapping(target = "informacionAdicional", ignore = true)
    @Mapping(target = "disponibilidad", ignore = true)
    PlanExclusivo planExclusivoCreateDtoToPlanExclusivo(PlanExclusivoCreateDTO planExclusivoCreateDTO);
}
