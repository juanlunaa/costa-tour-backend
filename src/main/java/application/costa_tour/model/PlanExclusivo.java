package application.costa_tour.model;

import application.costa_tour.dto.plan.DisponibilidadDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "plan_exclusivo")
@PrimaryKeyJoinColumn(name = "id_plan")
public class PlanExclusivo extends Plan {
    @Column(name = "expectativa")
    public String expectativa;

    @Column(name = "nota_ubicacion")
    public String notaUbicacion;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_plan_exclusivo")
    private List<ServicioIncluido> serviciosIncluidos;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_plan_exclusivo")
    private List<InformacionAdicional> informacionAdicional;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_plan_exclusivo")
    private List<DisponibilidadPlanExclusivo> disponibilidad;

    public void addServiciosIncluidos(List<String> serviciosIncluidos) {
        this.serviciosIncluidos = new ArrayList<>();

        for (String servicio : serviciosIncluidos) {
            this.serviciosIncluidos.add(new ServicioIncluido(servicio));
        }
    }

    public void addInformacionAdicional(List<String> informacionAdicional) {
        this.informacionAdicional = new ArrayList<>();

        for (String informacion : informacionAdicional) {
            this.informacionAdicional.add(new InformacionAdicional(informacion));
        }
    }

    public void addDisponibilidad(List<DisponibilidadDTO> disponibilidad) {
        this.disponibilidad = new ArrayList<>();

        for (DisponibilidadDTO disponibilidadDTO : disponibilidad) {
            disponibilidadDTO.getHoras().forEach(hora -> {
                this.disponibilidad.add(new DisponibilidadPlanExclusivo(disponibilidadDTO.getDia(), hora));
            });
        }
    }
}
