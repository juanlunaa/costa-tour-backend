package application.costa_tour.repository;

import application.costa_tour.model.PlanExclusivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanExclusivoRepository extends JpaRepository<PlanExclusivo, Long> {
}
