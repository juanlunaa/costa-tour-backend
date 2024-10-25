package application.costa_tour.dto.mapper;

import application.costa_tour.dto.AliadoDTO;
import application.costa_tour.model.Aliado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AliadoMapper {

    AliadoMapper mapper = Mappers.getMapper(AliadoMapper.class);

    @Mapping(source = "usuario.id", target = "userId")
    @Mapping(source = "usuario.tipo", target = "tipoUsuario")
    @Mapping(source = "usuario.email", target = "email")
    @Mapping(source = "usuario.fotoPerfil", target = "avatar")
    AliadoDTO aliadoToAliadoDto(Aliado aliado);
}
