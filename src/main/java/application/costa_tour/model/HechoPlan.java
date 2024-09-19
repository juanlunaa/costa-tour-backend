package application.costa_tour.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "hecho_plan")
public class HechoPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hecho_plan")
    private Long id;

    @Column(name = "descripcion")
    private String decripcion;
}
