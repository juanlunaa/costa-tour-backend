package application.costa_tour.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "imagen_plan")
public class ImagenPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imagen_plan")
    private Long id;

    @Column(name = "url")
    private String url;

//    @ManyToOne()
//    @JoinColumn(name = "id_plan")
//    private Plan plan;

    public ImagenPlan(String url) {
        this.url = url;
    }

}
