package application.costa_tour.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "caracteristica")
public class Caracteristica {
    @Id
    @Column(name = "id_caracteristica")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descripcion_caracteristica")
    private String descripcion;
}
