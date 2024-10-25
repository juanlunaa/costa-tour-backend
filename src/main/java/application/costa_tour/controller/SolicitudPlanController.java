package application.costa_tour.controller;

import application.costa_tour.dto.PlanCreateDTO;
import application.costa_tour.dto.SolicitudPlanDTO;
import application.costa_tour.dto.mapper.PlanCreateMapper;
import application.costa_tour.exception.SuccessResponse;
import application.costa_tour.model.*;
import application.costa_tour.model.enums.PublicationStatus;
import application.costa_tour.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/plan-request")
public class SolicitudPlanController {

    @Autowired
    private SolicitudPlanService solicitudPlanService;

    @Autowired
    private AliadoService aliadoService;

    @Autowired
    private AdministradorService administradorService;

    @Autowired
    private PlanService planService;

    @Autowired
    private StorageService storageService;

    @PostMapping("/create")
    public ResponseEntity<?> createSolicitudPlan (
            @ModelAttribute @Valid PlanCreateDTO planCreateDTO,
            @RequestParam String nitAliado
    ) {
        Aliado aliado = aliadoService.getAliadoEntityByNit(nitAliado);
        Plan plan = PlanCreateMapper.mapper.planCreateDtoToPlan(planCreateDTO);

        plan.addCaracteristicas(planCreateDTO.getCaracteristicas().stream()
                .map(ic -> new Caracteristica(ic))
                .collect(Collectors.toList())
        );

        plan.setEstadoPublicacion(PublicationStatus.PENDIENTE);

        SolicitudPlan solicitudPlan = solicitudPlanService.createSolicitud(plan, aliado);

        List<String> urls = storageService.savePlanImages(plan.getId(), planCreateDTO.getNombre(), planCreateDTO.getImagenesFiles());

        solicitudPlan.getPlan().addImagenes(urls);
        solicitudPlan.getPlan().setImagenMiniatura(urls.get(planCreateDTO.getMiniaturaSelect()));

        planService.createPlan(solicitudPlan.getPlan());

        SolicitudPlanDTO solicitudPlanDTO = solicitudPlanService.getSolicitudById(solicitudPlan.getId());

        return new ResponseEntity<>(
                solicitudPlanDTO,
                HttpStatus.OK);
    }

    @PostMapping("/process")
    public ResponseEntity<?> processSolicitudPlan (
            @RequestParam Long solicitudId,
            @RequestParam boolean aprobado,
            @RequestParam String comentario,
            @RequestParam Long adminId
    ) {
        Administrador administrador = administradorService.getAdministradorEntityById(adminId);

        solicitudPlanService.processSolicitud(solicitudId, aprobado, comentario, administrador);

        return new ResponseEntity<>(
                SuccessResponse
                        .builder()
                        .message(aprobado ? "Request approved successfully": "Request rejected successfully")
                        .build(),
                HttpStatus.OK);
    }
}
