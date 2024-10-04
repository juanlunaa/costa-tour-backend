package application.costa_tour.service;

import application.costa_tour.model.Favorito;
import application.costa_tour.repository.FavoritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    public Optional<Favorito> getFavoritoByTuristaAndPlan(String turistaDni, Long planId) {
        return favoritoRepository.findByTuristaDniAndPlanId(turistaDni, planId);
    }
}
