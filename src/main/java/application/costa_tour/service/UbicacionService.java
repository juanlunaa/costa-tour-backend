package application.costa_tour.service;

import application.costa_tour.model.Ubicacion;
import application.costa_tour.repository.UbicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UbicacionService {

    @Autowired
    UbicacionRepository ubicacionRepository;

    public Ubicacion getUbicacionEntity(Long id) {
        return ubicacionRepository.findById(id).orElse(null);
    }

    public void createUbicacion (Ubicacion ubicacion) {
        ubicacionRepository.save(ubicacion);
    }

    public void updateUbicacion (Long id, Ubicacion ubicacion) {
        Ubicacion previousUbicacion = ubicacionRepository.findById(id).orElse(null);

        previousUbicacion.setLatitud(ubicacion.getLatitud());
        previousUbicacion.setLongitud(ubicacion.getLongitud());
        previousUbicacion.setDireccion(ubicacion.getDireccion());

        ubicacionRepository.save(previousUbicacion);
        System.out.println(previousUbicacion.getId() + "<--");
    }
}
