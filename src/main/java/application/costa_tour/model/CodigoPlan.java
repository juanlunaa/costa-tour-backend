package application.costa_tour.model;

import application.costa_tour.model.enums.CodePlanStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "codigo_plan")
public class CodigoPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_codigo_plan")
    private Long id;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "fecha_generacion")
    private LocalDateTime fechaGeneracion;

    @Column(name = "fecha_expiracion")
    private LocalDateTime fechaExpiracion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_vigencia")
    private CodePlanStatus estadoVigencia;

    @ManyToOne
    @JoinColumn(name = "dni_turista")
    private Turista turista;

    @ManyToOne
    @JoinColumn(name = "id_plan")
    private Plan plan;
}
