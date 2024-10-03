package application.costa_tour.dto.mapper;

import application.costa_tour.dto.TuristaUpdateDTO;
import application.costa_tour.model.Turista;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TuristaUpdateMapper {
    TuristaUpdateMapper mapper = Mappers.getMapper(TuristaUpdateMapper.class);

    @Mapping(source = "idCiudad", target = "ciudad.id")
    Turista turistaUpdateDtoToTurista(TuristaUpdateDTO turistaUpdateDTO);
}
