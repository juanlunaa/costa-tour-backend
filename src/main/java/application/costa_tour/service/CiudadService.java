package application.costa_tour.service;

import application.costa_tour.model.Ciudad;
import application.costa_tour.repository.CiudadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CiudadService {

    @Autowired
    private CiudadRepository ciudadRepository;

    public boolean isExitsCity (Long id) {
        return ciudadRepository.existsById(id);
    }

    public Ciudad getCiudadById(Long id) {
        return ciudadRepository.findById(id).orElse(null);
    }
}
