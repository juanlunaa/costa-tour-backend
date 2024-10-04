package application.costa_tour.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "favorito")
public class Favorito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_favorito")
    private Long id;

    @Column(name = "dni_turista")
    private String turistaDni;

    @Column(name = "id_plan")
    private Long planId;

    @Column(name = "fecha_guardado")
    private LocalDateTime fechaGuardado;
}
