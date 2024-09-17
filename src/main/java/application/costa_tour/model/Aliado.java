package application.costa_tour.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "aliado")
public class Aliado {
    @Id
    @Column(name = "nit_aliado")
    private String nitAliado;

    @Column(name = "nombre_empresa")
    private String nombreEmpresa;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono")
    private String telefono;

    @OneToOne
    @JoinColumn(name="id_usuario")
    private Usuario usuario;
}
