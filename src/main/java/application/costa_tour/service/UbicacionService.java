package application.costa_tour.service;

import application.costa_tour.model.Ubicacion;
import application.costa_tour.repository.UbicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UbicacionService {

    @Autowired
    UbicacionRepository ubicacionRepository;

    public void createUbicacion (Ubicacion ubicacion) {
        ubicacionRepository.save(ubicacion);
    }
}
