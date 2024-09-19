package application.costa_tour.model;

import application.costa_tour.model.enums.PlanCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria")
    private PlanCategory categoria;

    @Column(name = "rango_min_dinero")
    private String rangoMinDinero;

    @Column(name = "rango_max_dinero")
    private String rangoMaxDinero;

    @Column(name = "imagen_miniatura")
    private String imagenMiniatura;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_ubicacion")
    private Ubicacion ubicacion;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_plan")
    private List<ImagenPlan> imagenes;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_plan")
    private List<HechoPlan> hechos;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CaracteristicaPlan> caracteristicasPlan;
}
