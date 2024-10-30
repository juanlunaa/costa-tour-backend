package application.costa_tour.repository;

import application.costa_tour.model.CodigoPlan;
import application.costa_tour.model.enums.CodePlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodigoPlanRepository extends JpaRepository<CodigoPlan, Long> {
    List<CodigoPlan> findByTuristaDni(String dni);
    Optional<CodigoPlan> findByCodigo(String codigo);
    Optional<CodigoPlan> findByTuristaDniAndPlanIdAndEstado(String dniTurista, Long planId, CodePlanStatus estado);
    boolean existsByCodigo(String codigo);
}
