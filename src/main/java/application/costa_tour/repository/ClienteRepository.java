package application.costa_tour.repository;

import application.costa_tour.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {

    @Query("SELECT c FROM Cliente c WHERE c.usuario.id = ?1 ")
    Optional<Cliente> findClienteByUsuarioId (Long userId);
}
