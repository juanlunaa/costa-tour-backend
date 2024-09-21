package application.costa_tour.dto.mapper;

import application.costa_tour.dto.InteresDTO;
import application.costa_tour.model.Interes;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface InteresMapper {

    InteresMapper mapper = Mappers.getMapper(InteresMapper.class);

    List<InteresDTO> interesesToInteresesDtos(List<Interes> intereses);

    InteresDTO interesToInteresDto(Interes interes);

}
