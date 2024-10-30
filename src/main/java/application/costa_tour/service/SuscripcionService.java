package application.costa_tour.service;

import application.costa_tour.exception.ResourceNotFoundException;
import application.costa_tour.exception.SubscriptionConflictException;
import application.costa_tour.model.Suscripcion;
import application.costa_tour.model.Usuario;
import application.costa_tour.model.UsuarioSuscripcion;
import application.costa_tour.model.enums.SubscriptionStatus;
import application.costa_tour.model.enums.SubscriptionUser;
import application.costa_tour.model.enums.UserRole;
import application.costa_tour.repository.SuscripcionRepository;
import application.costa_tour.repository.UsuarioSuscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class SuscripcionService {

    @Autowired
    private UsuarioSuscripcionRepository usuarioSuscripcionRepository;

    @Autowired
    private SuscripcionRepository suscripcionRepository;

    public Suscripcion getSubscriptionById(Long id) {
        return suscripcionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found for id= " + id));
    }

    public Suscripcion getSubscriptionByType(Long id, SubscriptionUser tipo) {
        return suscripcionRepository.findByIdAndTipo(id, tipo)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found for type= " + tipo));
    }

    public boolean hasActiveSubscription(Usuario usuario) {
        return usuarioSuscripcionRepository.findByUsuarioIdAndEstado(usuario.getId(), SubscriptionStatus.ACTIVA).isPresent();
    }

    public UsuarioSuscripcion crearSuscripcion(Usuario usuario, Long suscripcionId) {
        // Se verifica si el usuario ya tiene una suscripcion activa
        Optional<UsuarioSuscripcion> suscripcionExistente =
                usuarioSuscripcionRepository.findByUsuarioIdAndEstado(usuario.getId(), SubscriptionStatus.ACTIVA);

        if (suscripcionExistente.isPresent() && suscripcionExistente.get().isActive()) {
            throw new SubscriptionConflictException("El usuario ya tiene una suscripción activa");
        }

        // Se valida que el tipo de usuario sea válido para suscripción
        if (usuario.getTipo() != UserRole.ALIADO && usuario.getTipo() != UserRole.TURISTA) {
            throw new SubscriptionConflictException("Tipo de usuario no válido para suscripción");
        }

        UsuarioSuscripcion userSuscripcion = UsuarioSuscripcion.builder()
                .usuario(usuario)
                .suscripcion(getSubscriptionByType(suscripcionId, usuario.getTipo() == UserRole.ALIADO ? SubscriptionUser.ALIADO : SubscriptionUser.TURISTA))
                .estado(SubscriptionStatus.PENDIENTE)
                .idPago(generateExternalReference())
                .build();

        return usuarioSuscripcionRepository.save(userSuscripcion);
    }

    public UsuarioSuscripcion updatePaymentId(Long userSuscripcionId, String paymentId) {
        UsuarioSuscripcion userSuscripcion = usuarioSuscripcionRepository.findById(userSuscripcionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found for id= " + userSuscripcionId));

        userSuscripcion.setIdPago(paymentId);

        return usuarioSuscripcionRepository.save(userSuscripcion);
    }

    public UsuarioSuscripcion activarSuscripcion(String paymentId) {
        UsuarioSuscripcion userSuscripcion = usuarioSuscripcionRepository.findByIdPago(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found for payment id= " + paymentId));

        int durationDays = userSuscripcion.getSuscripcion().getDescripcion().equalsIgnoreCase("ANUAL") ? 365 : 30;

        LocalDateTime now = LocalDateTime.now();
        userSuscripcion.setEstado(SubscriptionStatus.ACTIVA);
        userSuscripcion.setFechaInicioPeriodo(now);
        userSuscripcion.setFechaFinPeriodo(now.plusDays(durationDays));

        return usuarioSuscripcionRepository.save(userSuscripcion);
    }

    public UsuarioSuscripcion cancelarSuscripcion(String paymentId) {
        UsuarioSuscripcion userSuscripcion = usuarioSuscripcionRepository.findByIdPago(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found for payment id= " + paymentId));

        userSuscripcion.setEstado(SubscriptionStatus.CANCELADA);
        userSuscripcion.setFechaCancelacion(LocalDateTime.now());

        return usuarioSuscripcionRepository.save(userSuscripcion);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void verificarSuscripcionesExpiradas() {
        List<UsuarioSuscripcion> suscripcionesExpiradas = usuarioSuscripcionRepository
                .findAll()
                .stream()
                .filter(s -> s.getEstado() == SubscriptionStatus.ACTIVA)
                .filter(s -> s.getFechaFinPeriodo().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());

        suscripcionesExpiradas.forEach(suscripcion -> {
            suscripcion.setEstado(SubscriptionStatus.EXPIRADA);
            usuarioSuscripcionRepository.save(suscripcion);
        });
    }

    private String generateExternalReference() {
        // Generar un ID único para trackear la transacción
        return "SUB-" + UUID.randomUUID().toString();
    }
}
