package application.costa_tour.dto.mapper;

import application.costa_tour.dto.AdministradorDTO;
import application.costa_tour.model.Administrador;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdministradorMapper {

    AdministradorMapper mapper = Mappers.getMapper(AdministradorMapper.class);

    @Mapping(source = "usuario.id", target = "userId")
    @Mapping(source = "usuario.tipo", target = "tipoUsuario")
    @Mapping(source = "usuario.nombre", target = "nombre")
    @Mapping(source = "usuario.apellido", target = "apellido")
    @Mapping(source = "usuario.email", target = "email")
    @Mapping(source = "usuario.telefono", target = "telefono")
    @Mapping(source = "usuario.imagenPerfil", target = "avatar")
    AdministradorDTO administradorToAdministradorDto (Administrador administrador);

}
