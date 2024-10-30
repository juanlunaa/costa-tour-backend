package application.costa_tour.repository;

import application.costa_tour.model.Suscripcion;
import application.costa_tour.model.enums.SubscriptionUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long> {
    Optional<Suscripcion> findByTipo(SubscriptionUser tipo);
    Optional<Suscripcion> findByIdAndTipo(Long id, SubscriptionUser tipo);
}
