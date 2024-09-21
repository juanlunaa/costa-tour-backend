package application.costa_tour.repository;

import application.costa_tour.model.Interes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InteresRepository extends JpaRepository<Interes, Long> {
}
