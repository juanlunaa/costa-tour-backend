package application.costa_tour.service;

import application.costa_tour.model.Usuario;
import application.costa_tour.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void createUser (Usuario usuario) {
        usuario.setImagenPerfil("http://localhost:4000/files/avatars/avatar-default.png");
        usuarioRepository.save(usuario);
    }

    public boolean isExistsUser (Long id) {
        return usuarioRepository.existsById(id);
    }

    public void updateUserAvatarPath (Long userId, String filename, String hostUrl) {
        String avatarUrl = ServletUriComponentsBuilder
                .fromHttpUrl(hostUrl)
                .path("/files/avatars/")
                .path(filename)
                .toUriString();

        Usuario usuario = usuarioRepository.findById(userId).orElse(null);

        usuario.setImagenPerfil(avatarUrl);

        usuarioRepository.save(usuario);
    }
}
