package application.costa_tour.dto.mapper;

import application.costa_tour.dto.PaisDTO;
import application.costa_tour.model.Pais;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PaisMapper {

    PaisMapper mapper = Mappers.getMapper(PaisMapper.class);

    List<PaisDTO> paisesToPaisesDto(List<Pais> paises);

    @Mapping(source = "nombre", target = "name")
    PaisDTO paisToPaisDto(Pais pais);
}
