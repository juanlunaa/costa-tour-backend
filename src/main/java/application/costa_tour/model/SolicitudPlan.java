package application.costa_tour.model;

import application.costa_tour.model.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "solicitud_plan")
public class SolicitudPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud_plan")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private RequestStatus estado;

    @Column(name = "fecha_solicitud")
    private LocalDateTime fechaSolicitud;

    @Column(name = "fecha_revision")
    private LocalDateTime fechaRevision;

    @Column(name = "comentario_revision")
    private String comentarioRevision;

    @ManyToOne
    @JoinColumn(name = "nit_aliado")
    private Aliado aliado;

    @ManyToOne
    @JoinColumn(name = "id_administrador")
    private Administrador administrador;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_plan")
    private Plan plan;
}
