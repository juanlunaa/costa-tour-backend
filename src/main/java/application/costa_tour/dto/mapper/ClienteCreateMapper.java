package application.costa_tour.dto.mapper;

import application.costa_tour.dto.ClienteCreateDTO;
import application.costa_tour.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClienteCreateMapper {

    ClienteCreateMapper mapper = Mappers.getMapper(ClienteCreateMapper.class);

    @Mapping(source = "tipoUsuario", target = "usuario.tipo")
    @Mapping(source = "nombre", target = "usuario.nombre")
    @Mapping(source = "apellido", target = "usuario.apellido")
    @Mapping(source = "telefono", target = "usuario.telefono")
    @Mapping(source = "email", target = "usuario.email")
    @Mapping(source = "password", target = "usuario.password")
    @Mapping(source = "idCiudad", target = "ciudad.id")
    @Mapping(source = "idTipoDocumento", target = "tipoDocumento.id")
    Cliente clienteCreateDtoToCliente (ClienteCreateDTO clienteCreateDTO);
}
