package application.costa_tour.model;

import application.costa_tour.model.enums.SubscriptionUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "suscripcion")
public class Suscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_suscripcion")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "suscripcion_usuario")
    private SubscriptionUser tipo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio")
    private Double precio;
}
