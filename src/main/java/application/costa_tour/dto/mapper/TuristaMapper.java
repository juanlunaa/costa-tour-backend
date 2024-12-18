package application.costa_tour.dto.mapper;

import application.costa_tour.dto.*;
import application.costa_tour.model.Turista;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TuristaMapper {

    TuristaMapper mapper = Mappers.getMapper(TuristaMapper.class);

    default TuristaDTO turistaToTuristaDto(Turista turista) {
        if (turista == null) {
            return null;
        }

        TuristaDTO turistaDTO = new TuristaDTO();

        turistaDTO.setUserId(turista.getUsuario().getId());
        turistaDTO.setDni(turista.getDni());
        turistaDTO.setTipoUsuario(turista.getUsuario().getTipo().toString());
        turistaDTO.setNombre(turista.getNombre());
        turistaDTO.setApellido(turista.getApellido());
        turistaDTO.setFechaNacimiento(turista.getFechaNacimiento());
        turistaDTO.setEdad(turista.getEdad());
        turistaDTO.setEmail(turista.getUsuario().getEmail());
        turistaDTO.setAvatar(turista.getUsuario().getFotoPerfil());

        turistaDTO.setCiudad(new CiudadDTO(
                turista.getCiudad().getId(),
                turista.getCiudad().getNombre()));

        turistaDTO.setEstado(new EstadoDTO(
                turista.getCiudad().getEstado().getId(),
                turista.getCiudad().getEstado().getNombre()
        ));

        turistaDTO.setPais(new PaisDTO(
                turista.getCiudad().getEstado().getPais().getId(),
                turista.getCiudad().getEstado().getPais().getNombre()
        ));

        turistaDTO.setPlanesFavoritos(turista.getPlanesFavoritos().stream()
                .map(pf -> pf.getPlanId())
                .collect(Collectors.toCollection(LinkedHashSet::new))
        );

        turistaDTO.setIntereses(turista.getInteresesTurista().stream()
                .map(it -> new InteresTuristaDTO(it.getInteres().getId(), it.getInteres().getPalabraClave()))
                .collect(Collectors.toList())
        );

        return turistaDTO;
    }

}
