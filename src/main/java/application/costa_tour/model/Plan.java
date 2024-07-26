package application.costa_tour.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "plan")
public class Plan {
    @Id
    @Column(name = "id_plan")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_plan")
    private String nombre;

    @Column(name = "descripcion_plan")
    private String descripcion;

    @Column(name = "categoria_plan")
    private String categoria;

    @Column(name = "rango_precio_plan")
    private String rangoPrecio;

    @Column(name = "imagen_card_plan")
    private String imagenCard;

    @Column(name = "imagenes_plan")
    private String imagenes;

    @OneToOne
    @JoinColumn(name = "id_ubicacion")
    private Ubicacion ubicacion;
}
