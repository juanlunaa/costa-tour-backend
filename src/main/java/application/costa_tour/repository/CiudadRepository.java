package application.costa_tour.repository;

import application.costa_tour.model.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Long> {

    @Query("SELECT c FROM Ciudad c WHERE c.estado.id = ?1 ORDER BY c.nombre")
    List<Ciudad> findCiudadesByEstadoId(Long id);
}
