package application.costa_tour.dto.mapper;

import application.costa_tour.dto.AliadoCreateDTO;
import application.costa_tour.model.Aliado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AliadoCreateMapper {

    AliadoCreateMapper mapper = Mappers.getMapper(AliadoCreateMapper.class);

    @Mapping(source = "email", target = "usuario.email")
    @Mapping(source = "password", target = "usuario.password")
    Aliado aliadoCreateDtoToAliado(AliadoCreateDTO aliadoCreateDTO);
}
