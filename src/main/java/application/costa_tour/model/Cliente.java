package application.costa_tour.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cliente")
public class Cliente{
    @Id
    @Column(name = "dni_cliente")
    private String dni;

    @Column(name = "fecha_nacimiento_cliente")
    private Date fechaNacimiento;

    @Column(name = "edad_cliente")
    private Integer edad;

    @ManyToOne
    @JoinColumn(name = "id_ciudad")
    private Ciudad ciudad;

    @ManyToOne
    @JoinColumn(name="id_tipo_documento")
    private TipoDocumento tipoDocumento;

    @OneToOne
    @JoinColumn(name="id_usuario")
    private Usuario usuario;
}
