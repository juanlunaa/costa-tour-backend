package application.costa_tour.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "interes_turista")
public class InteresTurista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interes_turista")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dni_turista")
    private Turista turista;

    @ManyToOne
    @JoinColumn(name = "id_interes")
    private Interes interes;

    public InteresTurista(Turista turista, Interes interes) {
        this.turista = turista;
        this.interes = interes;
    }
}
