package application.costa_tour.dto.mapper;

import application.costa_tour.dto.CaracteristicaDTO;
import application.costa_tour.model.Caracteristica;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CaracteristicaMapper {

    CaracteristicaMapper mapper = Mappers.getMapper(CaracteristicaMapper.class);

    List<CaracteristicaDTO> caracteristicasToCaracteristicasDtos(List<Caracteristica> caracteristicas);

    CaracteristicaDTO caracteristicaToCaracteristicaDto(CaracteristicaDTO caracteristica);
}
