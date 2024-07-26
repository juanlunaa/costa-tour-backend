package application.costa_tour.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "administrador")
public class Administrador{
    @Id
    @Column(name = "id_administrador")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rol_administrador")
    private String rol;

    @OneToOne
    @JoinColumn(name="id_usuario")
    private Usuario usuario;
}
