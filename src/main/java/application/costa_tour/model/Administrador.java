package application.costa_tour.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "administrador")
public class Administrador{
    @Id
    @Column(name = "id_administrador")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol_administrador")
    private AdminRole rol;

    @OneToOne
    @JoinColumn(name="id_usuario")
    private Usuario usuario;
}
