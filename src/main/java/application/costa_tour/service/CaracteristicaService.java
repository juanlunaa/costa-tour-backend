package application.costa_tour.service;

import application.costa_tour.model.Caracteristica;
import application.costa_tour.repository.CaracteristicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaracteristicaService {

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    public Caracteristica getCaracteristica (Long id) {
        return caracteristicaRepository.findById(id).orElse(null);
    }

}
