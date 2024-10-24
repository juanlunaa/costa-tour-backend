package application.costa_tour.repository;

import application.costa_tour.model.UsuarioSuscripcion;
import application.costa_tour.model.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioSuscripcionRepository extends JpaRepository<UsuarioSuscripcion, Long> {
    Optional<UsuarioSuscripcion> findByUsuarioId(Long userId);
    Optional<UsuarioSuscripcion> findByUsuarioIdAndEstado(Long userId, SubscriptionStatus estado);
    Optional<UsuarioSuscripcion> findByIdPago(String idPago);
}
