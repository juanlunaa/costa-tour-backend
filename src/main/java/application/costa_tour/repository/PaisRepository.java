package application.costa_tour.repository;

import application.costa_tour.model.Pais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaisRepository extends JpaRepository<Pais, Long> {

    @Query("SELECT p FROM Pais p ORDER BY p.nombre")
    List<Pais> findAllOrder();
}
