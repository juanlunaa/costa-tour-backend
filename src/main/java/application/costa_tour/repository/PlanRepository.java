package application.costa_tour.repository;

import application.costa_tour.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    @Query("SELECT p.nombre FROM Plan p WHERE p.id = ?1")
    String findPlanNameByIdPlan(Long id);
}
