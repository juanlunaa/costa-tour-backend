package application.costa_tour.service;

import application.costa_tour.dto.AdministradorDTO;
import application.costa_tour.dto.AdministradorUpdateDTO;
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

    public Administrador getAdministradorEntityById(Long adminId) {
        return administradorRepository.findById(adminId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String
                                .format("Administrador not found for id=%s", adminId)));
    }

    public AdministradorDTO getAdminByUser (Usuario user) {
        Administrador admin = administradorRepository.findAdministradorByUsuarioId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(String
                                .format("Administrator not found for user id=%s", user.getId())));

        return AdministradorMapper.mapper.administradorToAdministradorDto(admin);
    }

    public boolean isExistingAdminByUserId(Long userId) {
        return administradorRepository.findAdministradorByUsuarioId(userId).orElse(null) != null;
    }

    public void createAdmin (Administrador admin) {
        administradorRepository.save(admin);
    }

    public AdministradorDTO updateAdmin(Long userId, AdministradorUpdateDTO administradorDTO) {
        Administrador previousAdmin = administradorRepository.findAdministradorByUsuarioId(userId).orElse(null);

        previousAdmin.setNombre(administradorDTO.getNombre());
        previousAdmin.setApellido(administradorDTO.getApellido());

        Administrador adminUpdated = administradorRepository.save(previousAdmin);

        return AdministradorMapper.mapper.administradorToAdministradorDto(adminUpdated);
    }
}
