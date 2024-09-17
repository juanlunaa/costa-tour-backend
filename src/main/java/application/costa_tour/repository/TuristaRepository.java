package application.costa_tour.repository;

import application.costa_tour.model.Turista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TuristaRepository extends JpaRepository<Turista, String> {

    @Query("SELECT t FROM Turista t WHERE t.usuario.id = ?1 ")
    Optional<Turista> findTuristaByUsuarioId (Long userId);
}
