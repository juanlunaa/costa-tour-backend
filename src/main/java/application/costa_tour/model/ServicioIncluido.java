package application.costa_tour.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "servicio_incluido")
public class ServicioIncluido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio_incluido")
    private Long id;

    @Column(name = "servicio")
    private String servicio;

    public ServicioIncluido(String servicio) {
        this.servicio = servicio;
    }
}
