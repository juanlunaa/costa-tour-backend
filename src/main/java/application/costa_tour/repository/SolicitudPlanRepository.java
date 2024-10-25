package application.costa_tour.repository;

import application.costa_tour.model.SolicitudPlan;
import application.costa_tour.model.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudPlanRepository extends JpaRepository<SolicitudPlan, Long> {
    List<SolicitudPlan> findByEstado(RequestStatus estado);
    List<SolicitudPlan> findByAliadoNitAliado(String nitAliado);
}
