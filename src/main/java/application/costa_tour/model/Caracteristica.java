package application.costa_tour.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "caracteristica")
public class Caracteristica {
    @Id
    @Column(name = "id_caracteristica")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "palabra_clave")
    private String palabraClave;

    public Caracteristica(Long id) {
        this.id = id;
    }
}
