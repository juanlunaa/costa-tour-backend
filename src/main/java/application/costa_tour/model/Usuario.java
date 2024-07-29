package application.costa_tour.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @Column(name = "id_usuario")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario")
    private UserRole tipo;

    @Column(name = "nombre_usuario")
    private String nombre;

    @Column(name = "apellido_usuario")
    private String apellido;

    @Column(name = "email_usuario")
    private String email;

    @Column(name = "telefono_usuario")
    private String telefono;

    @Column(name = "password_usuario")
    private String password;

    @Column(name = "image_perfil_usuario")
    private String imagenPerfil;
}
