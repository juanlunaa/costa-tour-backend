package application.costa_tour.repository;

import application.costa_tour.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {

    @Query("SELECT a FROM Administrador a WHERE a.usuario.id = ?1 ")
    Optional<Administrador> findAdministradorByUsuarioId (Long userId);
}
