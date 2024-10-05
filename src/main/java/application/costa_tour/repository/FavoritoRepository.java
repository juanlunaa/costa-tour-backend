package application.costa_tour.repository;

import application.costa_tour.model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    Optional<Favorito> findByTuristaDniAndPlanId(String turistaDni, Long planId);

    boolean existsByTuristaDniAndPlanId(String turistaDni, Long planId);
}
