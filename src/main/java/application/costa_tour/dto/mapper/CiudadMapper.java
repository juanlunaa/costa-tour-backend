package application.costa_tour.dto.mapper;

import application.costa_tour.dto.CiudadDTO;
import application.costa_tour.model.Ciudad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CiudadMapper {

    CiudadMapper mapper = Mappers.getMapper(CiudadMapper.class);

    List<CiudadDTO> ciudadesToCiudadesDto(List<Ciudad> ciudades);

    @Mapping(source = "nombre", target = "name")
    CiudadDTO ciudadToCiudadDto(Ciudad ciudad);
}
