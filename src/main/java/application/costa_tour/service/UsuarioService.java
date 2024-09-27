package application.costa_tour.service;

import application.costa_tour.exception.InvalidCredentialsException;
import application.costa_tour.exception.ResourceNotFoundException;
import application.costa_tour.jwt.JwtService;
import application.costa_tour.model.Usuario;
import application.costa_tour.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public Usuario getUserByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not exists"));
    }

    public Pair<String, Usuario> credentialsValidate (String email, String password) {
//        Usuario user = usuarioRepository.findByEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("Email not exists"));
//
//        if (!user.getPassword().equals(password)) {
//            throw new InvalidCredentialsException("Password is invalid");
//        }
//
//        return user;
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not exists"));

        return Pair.of(jwtService.getToken(usuario), usuario);
    }

    public void createUser (Usuario usuario) {
        usuario.setFotoPerfil("/files/avatars/avatar-default.png");
        usuarioRepository.save(usuario);
    }

    public boolean isExistsUser (Long id) {
        return usuarioRepository.existsById(id);
    }

    public boolean isExitsAccountWithEmail (String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

        return usuario != null;
    }

    public void updateUserAvatarPath (Long userId, String filename) {
        String avatarUrl = "/files/avatars/" + filename;

        Usuario usuario = usuarioRepository.findById(userId).orElse(null);

        usuario.setFotoPerfil(avatarUrl);

        usuarioRepository.save(usuario);
    }
}
