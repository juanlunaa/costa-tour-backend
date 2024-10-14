package application.costa_tour.model;

import application.costa_tour.model.enums.DayWeek;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "disponibilidad_plan_exclusivo")
public class DisponibilidadPlanExclusivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_disponibilidad")
    private Long id;

    @Column(name = "dia_semana")
    @Enumerated(EnumType.STRING)
    private DayWeek diaSemana;

    @Column(name = "hora")
    private LocalTime hora;

    public DisponibilidadPlanExclusivo(DayWeek diaSemana, LocalTime hora) {
        this.diaSemana = diaSemana;
        this.hora = hora;
    }
}
