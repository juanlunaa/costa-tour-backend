package application.costa_tour.dto.mapper;

import application.costa_tour.dto.ClienteDTO;
import application.costa_tour.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClienteMapper {

    ClienteMapper mapper = Mappers.getMapper(ClienteMapper.class);

    @Mapping(source = "usuario.id", target = "userId")
    @Mapping(source = "usuario.tipo", target = "tipoUsuario")
    @Mapping(source = "usuario.nombre", target = "nombre")
    @Mapping(source = "usuario.apellido", target = "apellido")
    @Mapping(source = "usuario.email", target = "email")
    @Mapping(source = "usuario.telefono", target = "telefono")
    @Mapping(source = "usuario.imagenPerfil", target = "avatar")
    @Mapping(source = "tipoDocumento.descripcion", target = "tipoDocumento")
    @Mapping(source = "ciudad.id", target = "ciudad.id")
    @Mapping(source = "ciudad.nombre", target = "ciudad.name")
    @Mapping(source = "ciudad.estado.id", target = "estado.id")
    @Mapping(source = "ciudad.estado.nombre", target = "estado.name")
    @Mapping(source = "ciudad.estado.pais.id", target = "pais.id")
    @Mapping(source = "ciudad.estado.pais.nombre", target = "pais.name")
    ClienteDTO clienteToClienteDto (Cliente cliente);

}
