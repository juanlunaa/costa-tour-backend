package application.costa_tour.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_feedback")
    private Long id;

    @Column(name = "comentario")
    private String comentario;

    @Column(name = "calificacion")
    private int calificacion;

    @OneToOne
    @JoinColumn(name = "id_codigo_plan")
    private CodigoPlan codigoPlan;
}
