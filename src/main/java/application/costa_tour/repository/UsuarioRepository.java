package application.costa_tour.repository;

import application.costa_tour.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail (String email);

    @Query("SELECT u.email FROM Usuario u WHERE u.id = ?1")
    Optional<String> findEmailUsuarioByUsuarioId(Long userId);
}
