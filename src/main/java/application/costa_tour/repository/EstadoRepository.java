package application.costa_tour.repository;

import application.costa_tour.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {

    @Query("SELECT e FROM Estado e WHERE e.pais.id = ?1 ORDER BY e.nombre")
    List<Estado> findEstadosByPaisId(Long id);
}
