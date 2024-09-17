package application.costa_tour.dto.mapper;

import application.costa_tour.dto.TuristaCreateDTO;
import application.costa_tour.model.Turista;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TuristaCreateMapper {

    TuristaCreateMapper mapper = Mappers.getMapper(TuristaCreateMapper.class);

    @Mapping(source = "email", target = "usuario.email")
    @Mapping(source = "password", target = "usuario.password")
//    @Mapping(source = "idCiudad", target = "ciudad.id")
    Turista turistaCreateDtoToTurista(TuristaCreateDTO turistaCreateDto);
}
