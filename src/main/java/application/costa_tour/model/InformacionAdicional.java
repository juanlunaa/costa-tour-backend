package application.costa_tour.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "informacion_adicional")
public class InformacionAdicional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_informacion_adicional")
    private Long id;

    @Column(name = "descripcion")
    private String descripcion;

    public InformacionAdicional(String descripcion) {
        this.descripcion = descripcion;
    }
}
