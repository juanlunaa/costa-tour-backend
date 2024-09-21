package application.costa_tour.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "interes")
public class Interes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interes")
    private Long id;

    @Column(name = "palabra_clave")
    private String palabraClave;

    public Interes(Long id) {
        this.id = id;
    }
}
