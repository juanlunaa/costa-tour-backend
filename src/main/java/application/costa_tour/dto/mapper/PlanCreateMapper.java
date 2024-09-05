package application.costa_tour.dto.mapper;

import application.costa_tour.dto.PlanCreateDTO;
import application.costa_tour.model.Plan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlanCreateMapper {

    PlanCreateMapper mapper = Mappers.getMapper(PlanCreateMapper.class);

    @Mapping(source = "latitud", target = "ubicacion.latitud")
    @Mapping(source = "longitud", target = "ubicacion.longitud")
    @Mapping(source = "direccion", target = "ubicacion.direccion")
    Plan planCreateDtoToPlan (PlanCreateDTO planCreateDTO);
}
