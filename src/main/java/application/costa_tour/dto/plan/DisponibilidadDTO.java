package application.costa_tour.dto.plan;

import application.costa_tour.model.enums.DayWeek;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisponibilidadDTO {
    private DayWeek dia;
    private List<LocalTime> horas;
}
