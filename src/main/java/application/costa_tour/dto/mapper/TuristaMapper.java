package application.costa_tour.dto.mapper;

import application.costa_tour.dto.TuristaDTO;
import application.costa_tour.model.Turista;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TuristaMapper {

    TuristaMapper mapper = Mappers.getMapper(TuristaMapper.class);

    @Mapping(source = "usuario.id", target = "userId")
    @Mapping(source = "usuario.tipo", target = "tipoUsuario")
    @Mapping(source = "usuario.email", target = "email")
    @Mapping(source = "usuario.fotoPerfil", target = "avatar")
    @Mapping(source = "ciudad.id", target = "ciudad.id")
    @Mapping(source = "ciudad.nombre", target = "ciudad.name")
    @Mapping(source = "ciudad.estado.id", target = "estado.id")
    @Mapping(source = "ciudad.estado.nombre", target = "estado.name")
    @Mapping(source = "ciudad.estado.pais.id", target = "pais.id")
    @Mapping(source = "ciudad.estado.pais.nombre", target = "pais.name")
    TuristaDTO turistaToTuristaDto(Turista turista);

}
