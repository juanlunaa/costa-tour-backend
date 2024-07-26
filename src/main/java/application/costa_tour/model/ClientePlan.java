package application.costa_tour.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cliente_plan")
public class ClientePlan {
    @Id
    @Column(name="id_cliente_plan")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="fecha_plan")
    private String fecha;

    @Column(name="hora_plan")
    private String hora;

    @Column(name="estado_cliente_plan")
    private String estado;

    @ManyToOne
    @JoinColumn(name="dni_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name="id_plan")
    private Plan plan;
}
