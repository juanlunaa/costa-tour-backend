package application.costa_tour.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "turista")
public class Turista {
    @Id
    @Column(name = "dni_turista")
    private String dni;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;

    @Column(name = "edad")
    private Integer edad;

    @ManyToOne
    @JoinColumn(name = "id_ciudad")
    private Ciudad ciudad;

    @OneToOne
    @JoinColumn(name="id_usuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "turista", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InteresTurista> interesesTurista;

    public void addIntereses(List<Interes> intereses) {
        this.interesesTurista = new ArrayList<>();

        for (Interes interes : intereses) {
            InteresTurista newRelacionIt = new InteresTurista(this, interes);
            interesesTurista.add(newRelacionIt);
        }
    }

    public void updateIntereses(List<Interes> newIntereses) {
        Map<Long, Interes> newInteresesMap = newIntereses.stream()
                .collect(Collectors.toMap(i -> i.getId(), i -> i));

        interesesTurista.removeIf(it ->
                !newInteresesMap.containsKey(it.getInteres().getId()));

        for (Interes newInteres : newIntereses) {
            boolean exits = interesesTurista.stream()
                    .anyMatch(it -> it.getInteres().getId() == newInteres.getId());

            if (!exits) {
                InteresTurista newRelacionIt = new InteresTurista(this, newInteres);
                interesesTurista.add(newRelacionIt);
            }
        }
    }
}
