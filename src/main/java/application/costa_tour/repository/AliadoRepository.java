package application.costa_tour.repository;

import application.costa_tour.model.Aliado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AliadoRepository extends JpaRepository<Aliado, String> {

    @Query("SELECT a FROM Aliado a WHERE a.usuario.id = ?1")
    Optional<Aliado> findAliadoByUsuarioId(Long userId);
}
