package application.costa_tour.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "caracteristica_plan")
public class CaracteristicaPlan {
    @Id
    @Column(name = "id_caracteristica_plan")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_plan")
    private Plan plan;

    @ManyToOne
    @JoinColumn(name = "id_caracteristica")
    private Caracteristica caracteristica;

    public CaracteristicaPlan(Plan plan, Caracteristica caracteristica) {
        this.plan = plan;
        this.caracteristica = caracteristica;
    }
}
