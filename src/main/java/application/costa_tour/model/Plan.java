package application.costa_tour.model;

import application.costa_tour.model.enums.PlanCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public void updateCaracteristicas(List<Caracteristica> newCaracteristicas) {
//      transformo las nuevas caracteristicas (List -> Map) a un map
//      para hacer la comparacion mas facil
        Map<Long, Caracteristica> newCaracteristicasMap = newCaracteristicas.stream()
                .collect(Collectors.toMap(c -> c.getId(), c -> c));

//      remuevo de las caracteristicas del plan que no hacen parte de las nuevas
//      este caso ocurre cuando al editar el plan se eliminan caracteristicas

//      remuevo de caracteristicasPlan si una caracteristica de esta lista no hace
//      parte del map, esta comparacion se hace en base al id de la caracteristica
        caracteristicasPlan.removeIf(cp ->
                !newCaracteristicasMap.containsKey(cp.getCaracteristica().getId()));

//      añado las nuevas caracteristicas a la lista del plan
//      este caso ocurre cuando al editar el plan se añaden nuevas caracteristicas
        for (Caracteristica newCaracteristica : newCaracteristicas) {
            boolean exits = caracteristicasPlan.stream()
                    .anyMatch(cp -> cp.getCaracteristica().getId() == newCaracteristica.getId());

            if (!exits) {
                CaracteristicaPlan newRelacionCp = new CaracteristicaPlan(this, newCaracteristica);
                caracteristicasPlan.add(newRelacionCp);
            }
        }
    }

    public void updateImagenesPlan(List<String> newImagenesPlan) {
        Map<String, String> newImagenesPlanMap = newImagenesPlan.stream()
                .collect(Collectors.toMap(ip -> ip, ip -> ip));

        imagenes.removeIf(ip ->
                !newImagenesPlanMap.containsKey(ip.getUrl()));

        for (String newImage : newImagenesPlan) {
            boolean exists = imagenes.stream().anyMatch(ip -> ip.getUrl().equals(newImage));

            if (!exists) {
                imagenes.add(new ImagenPlan(newImage));
            }
        }
    }
}
