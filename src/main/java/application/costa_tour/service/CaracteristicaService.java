package application.costa_tour.service;

import application.costa_tour.exception.ResourceNotFoundException;
import application.costa_tour.model.Caracteristica;
import application.costa_tour.repository.CaracteristicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CaracteristicaService {

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    public Caracteristica getCaracteristica (Long id) {
        return caracteristicaRepository.findById(id).orElse(null);
    }

    public void isExistsCaracteristicas (List<Long> idCaracteristicasList) {
        Map<Long, Caracteristica> caracteristicasMap = caracteristicaRepository.findAll().stream()
                .collect(Collectors.toMap(Caracteristica::getId, c -> c));

        for (Long id : idCaracteristicasList) {
            if (!caracteristicasMap.containsKey(id)) {
                throw new ResourceNotFoundException(String
                        .format("Caracteristica not found for id=%s", id));
            }
        }
    }

}
