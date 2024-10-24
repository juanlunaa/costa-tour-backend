package application.costa_tour.model;

import application.costa_tour.model.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuario_suscripcion")
public class UsuarioSuscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario_suscripcion")
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_suscripcion", nullable = false)
    private Suscripcion suscripcion;

    @Column(name = "id_pago")
    private String idPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_suscripcion", nullable = false)
    private SubscriptionStatus estado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_inicio_periodo")
    private LocalDateTime fechaInicioPeriodo;

    @Column(name = "fecha_fin_periodo")
    private LocalDateTime fechaFinPeriodo;

    @Column(name = "fecha_cancelacion")
    private LocalDateTime fechaCancelacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        estado = SubscriptionStatus.PENDIENTE;
    }

    public boolean isActive() {
        return estado == SubscriptionStatus.ACTIVA &&
                fechaFinPeriodo != null &&
                fechaFinPeriodo.isAfter(LocalDateTime.now());
    }
}
