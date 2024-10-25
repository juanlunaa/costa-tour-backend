package application.costa_tour.service;

import application.costa_tour.dto.SolicitudPlanDTO;
import application.costa_tour.dto.mapper.SolicitudPlanMapper;
import application.costa_tour.exception.BadRequestException;
import application.costa_tour.exception.ResourceNotFoundException;
import application.costa_tour.model.Administrador;
import application.costa_tour.model.Aliado;
import application.costa_tour.model.Plan;
import application.costa_tour.model.SolicitudPlan;
import application.costa_tour.model.enums.PublicationStatus;
import application.costa_tour.model.enums.RequestStatus;
import application.costa_tour.repository.SolicitudPlanRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SolicitudPlanService {

    @Autowired
    private SolicitudPlanRepository solicitudPlanRepository;

    @Autowired
    private SolicitudPlanMapper solicitudPlanMapper;

    @Autowired
    private EntityManager entityManager;

    public SolicitudPlanDTO getSolicitudById(Long id) {
        entityManager.flush();
        entityManager.clear();
        SolicitudPlan solicitudPlan = solicitudPlanRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Request not found for id=" + id));

        return solicitudPlanMapper.solicitudPlanToSolicitudPlanDto(solicitudPlan);
    }

    public SolicitudPlan createSolicitud(Plan plan, Aliado aliado) {
        SolicitudPlan solicitud = new SolicitudPlan();
        solicitud.setPlan(plan);
        solicitud.setAliado(aliado);
        solicitud.setEstado(RequestStatus.PENDIENTE);
        solicitud.setFechaSolicitud(LocalDateTime.now());

        return solicitudPlanRepository.save(solicitud);
    }

    public List<SolicitudPlan> getSolicitudesPendientes() {
        return solicitudPlanRepository.findByEstado(RequestStatus.PENDIENTE);
    }

    public void processSolicitud(Long solicitudId,
                                 boolean aprobado,
                                 String comentario,
                                 Administrador administrador
    ) {
        SolicitudPlan solicitud = solicitudPlanRepository.findById(solicitudId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found for id=" + solicitudId));

        if (solicitud.getEstado() != RequestStatus.PENDIENTE) {
            throw new BadRequestException("Request is not pending");
        }

        solicitud.setAdministrador(administrador);
        solicitud.setFechaRevision(LocalDateTime.now());
        solicitud.setComentarioRevision(comentario);

        if (aprobado) {
            solicitud.setEstado(RequestStatus.APROBADO);
            solicitud.getPlan().setEstadoPublicacion(PublicationStatus.PUBLICADO);
        } else {
            solicitud.setEstado(RequestStatus.RECHAZADO);
            solicitud.getPlan().setEstadoPublicacion(PublicationStatus.RECHAZADO);
        }

        solicitudPlanRepository.save(solicitud);
    }

    public List<SolicitudPlan> getHistorySolicitudesAliado(String nitAliado) {
        return solicitudPlanRepository.findByAliadoNitAliado(nitAliado);
    }
}
