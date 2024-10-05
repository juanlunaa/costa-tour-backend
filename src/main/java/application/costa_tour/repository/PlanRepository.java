package application.costa_tour.repository;

import application.costa_tour.model.Plan;
import application.costa_tour.model.enums.PlanCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    @Query("SELECT p.nombre FROM Plan p WHERE p.id = ?1")
    String findPlanNameByIdPlan(Long id);

    @Query("SELECT p FROM Plan p WHERE p.categoria = ?1")
    List<Plan> findPlansByCategoria(PlanCategory categoria);

    @Query("""
            SELECT p
            FROM Plan p
            JOIN Favorito f
            ON p.id = f.planId
            WHERE f.turistaDni = ?1
            ORDER BY f.fechaGuardado DESC
            """)
    List<Plan> findPlansFavoritesByTuristaDni(String turistaDni);

//    @Query("SELECT AVG(f.calificacion) FROM Feedback f JOIN f.codigoPlan cp WHERE cp.plan.id = ?1")
//    Double getAverageRatingByPlan(Long planId);
}
