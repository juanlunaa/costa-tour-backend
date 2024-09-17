package application.costa_tour.repository;

import application.costa_tour.model.CaracteristicaPlan;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaracteristicaPlanRepository extends JpaRepository<CaracteristicaPlan, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM CaracteristicaPlan c WHERE c.plan.id = ?1")
    void removeAllCaracteristicaPlanByPlan (Long idPlan);

    @Query("SELECT c FROM CaracteristicaPlan c WHERE c.plan.id = ?1")
    List<CaracteristicaPlan> findCaracteristicaPlanListByPlanId (Long idPlan);
}
