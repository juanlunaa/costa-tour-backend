package application.costa_tour.dto.mapper;

import application.costa_tour.dto.AdministradorCreateDTO;
import application.costa_tour.model.Administrador;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdministradorCreateMapper {

    AdministradorCreateMapper mapper = Mappers.getMapper(AdministradorCreateMapper.class);

    @Mapping(source = "nombre", target = "usuario.nombre")
    @Mapping(source = "apellido", target = "usuario.apellido")
    @Mapping(source = "telefono", target = "usuario.telefono")
    @Mapping(source = "email", target = "usuario.email")
    @Mapping(source = "password", target = "usuario.password")
    Administrador administradorCreateDtoToAdministrador (AdministradorCreateDTO administradorCreateDTO);
}
