package application.costa_tour.service;

import application.costa_tour.dto.AdministradorDTO;
import application.costa_tour.dto.mapper.AdministradorMapper;
import application.costa_tour.exception.ResourceNotFoundException;
import application.costa_tour.model.Administrador;
import application.costa_tour.model.Usuario;
import application.costa_tour.repository.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    public AdministradorDTO getAdminByUser (Usuario user) {
        Administrador admin = administradorRepository.findAdministradorByUsuarioId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(String
                                .format("Administrator not found for user=%s %s", user.getNombre(), user.getApellido())));

        return AdministradorMapper.mapper.administradorToAdministradorDto(admin);
    }

    public void createAdmin (Administrador admin) {
        administradorRepository.save(admin);
    }
}
