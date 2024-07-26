package application.costa_tour.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="ubicacion")
public class Ubicacion {
    @Id
    @Column(name="id_ubicacion")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="latitud_ubicacion")
    private float latitud;

    @Column(name="longitud_ubicacion")
    private float longitud;

    @Column(name="direccion_ubicacion")
    private String direccion;
}
