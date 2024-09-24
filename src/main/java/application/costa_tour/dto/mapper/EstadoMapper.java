package application.costa_tour.dto.mapper;

import application.costa_tour.dto.EstadoDTO;
import application.costa_tour.model.Estado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface EstadoMapper {
    EstadoMapper mapper = Mappers.getMapper(EstadoMapper.class);

    List<EstadoDTO> estadosToEstadosDto(List<Estado> paises);

    @Mapping(source = "nombre", target = "name")
    EstadoDTO estadoToEstadoDto(Estado pais);
}
