package application.costa_tour.controller;

import application.costa_tour.dto.PlanCreateDTO;
import application.costa_tour.dto.PlanDTO;
import application.costa_tour.dto.TuristaDTO;
import application.costa_tour.dto.mapper.PlanCreateMapper;
import application.costa_tour.dto.mapper.plan.PlanExclusivoCreateMapper;
import application.costa_tour.dto.plan.PlanExclusivoCreateDTO;
import application.costa_tour.exception.BadRequestException;
import application.costa_tour.exception.ResourceNotFoundException;
import application.costa_tour.exception.SuccessResponse;
import application.costa_tour.model.*;
import application.costa_tour.model.enums.PlanCategory;
import application.costa_tour.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/plan")
public class PlanController {

    @Autowired
    private PlanService planService;

    @Autowired
    private PlanExclusivoService planExclusivoService;

    @Autowired
    private TuristaService turistaService;

    @Autowired
    private CaracteristicaPlanService caracteristicaPlanService;

    @Autowired
    private CaracteristicaService caracteristicaService;

    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private StorageService storageService;

    @GetMapping("/all")
    public ResponseEntity<?> getPlanes () {
        return ResponseEntity.ok(planService.getAllPlans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlan (@PathVariable("id") Long id) {
        return ResponseEntity.ok(planService.getPlan(id));
    }

    // falta añadir el tema de los hechos para los planes de categoria SITIOS_TURISTICOS
    @PostMapping("/create")
    public ResponseEntity<?> createPlan (@ModelAttribute @Valid PlanCreateDTO planCreateDTO) {

        createAndUpdateValidations(
                planCreateDTO.getCaracteristicas(),
                planCreateDTO.getMiniaturaSelect(),
                planCreateDTO.getImagenesFiles().size());

        Plan plan = PlanCreateMapper.mapper.planCreateDtoToPlan(planCreateDTO);

        plan.addCaracteristicas(planCreateDTO.getCaracteristicas().stream()
                .map(ic -> new Caracteristica(ic))
                .collect(Collectors.toList())
        );

        plan = planService.createPlan(plan);

        List<String> urls = storageService.savePlanImages(plan.getId(), planCreateDTO.getNombre(), planCreateDTO.getImagenesFiles());

        plan.addImagenes(urls);
        plan.setImagenMiniatura(urls.get(planCreateDTO.getMiniaturaSelect()));

        planService.createPlan(plan);

        return new ResponseEntity<>(
                SuccessResponse
                        .builder()
                        .message("Plan created successfully")
                        .build(),
                HttpStatus.OK);
    }

    @PutMapping("/update/{idPlan}")
    public ResponseEntity<?> updatePlan (
            @PathVariable("idPlan") Long idPlan,
            @ModelAttribute PlanCreateDTO planCreateDTO
    ) {

        if (!planService.isPlanExists(idPlan)) {
            throw new ResourceNotFoundException(String
                    .format("Plan not found for id=%s", idPlan));
        }

//        createAndUpdateValidations(
//                planCreateDTO.getCaracteristicas(),
//                planCreateDTO.getMiniaturaSelect(),
//                planCreateDTO.getImagenesFiles().size());

        Plan plan = PlanCreateMapper.mapper.planCreateDtoToPlan(planCreateDTO);
        String prevPlanName = planService.getNombrePlan(idPlan);

        List<Caracteristica> caracteristicas = planCreateDTO.getCaracteristicas().stream()
                .map(c -> new Caracteristica(c))
                .collect(Collectors.toList());

        List<String> urls =
                storageService.updatePlanImages(
                        idPlan,
                        prevPlanName,
                        plan.getNombre(),
                        planCreateDTO.getImagenesExistentes(),
                        planCreateDTO.getImagenesFiles());

        plan.setImagenMiniatura(urls.get(planCreateDTO.getMiniaturaSelect()));

        planService.updatePlan(idPlan, plan, caracteristicas, urls);

        return new ResponseEntity<>(
                SuccessResponse
                        .builder()
                        .message("Plan updated successfully")
                        .build(),
                HttpStatus.OK);
    }

    @DeleteMapping("/delete/{idPlan}")
    public ResponseEntity<?> deletePlan (@PathVariable("idPlan") Long idPlan) {

        if (!planService.isPlanExists(idPlan)) {
            throw new ResourceNotFoundException(String
                    .format("Plan not found for id=%s", idPlan));
        }

        String planName = planService.getNombrePlan(idPlan);
        planService.deletePlan(idPlan);
        storageService.deleteFolderPlan(idPlan, planName);

        return new ResponseEntity<>(
                SuccessResponse
                        .builder()
                        .message("Plan deleted successfully")
                        .build(),
                HttpStatus.OK);
    }

    @GetMapping("/recomendation")
    public ResponseEntity<?> getPlanRecomendation(
            @RequestParam String dniTurista
    ) {
        PlanCategory[] categories = PlanCategory.values();
        List<PlanDTO> recomendaciones = new ArrayList<>();
        TuristaDTO turista = turistaService.getTuristaByDni(dniTurista);

        for (PlanCategory cat : categories) {
            if (!cat.equals(PlanCategory.EXTREMO)) {
                List<PlanDTO> planes = planService.getPlansByCategoria(cat);

                List<PlanDTO> planesRecomendados = planService.planRecomendation(
                        planes,
                        turista.getIntereses(),
                        3
                );

                recomendaciones.addAll(planesRecomendados);
            }

        }

        return ResponseEntity.ok(recomendaciones);
    }

    @GetMapping("/exclusive/all")
    public ResponseEntity<?> getPlanesExclusivos () {
        return ResponseEntity.ok(planExclusivoService.getAllPlanExclusivo());
    }

    @PostMapping("/exclusive/create")
    public ResponseEntity<?> createPlanExclusivo (@ModelAttribute @Valid PlanExclusivoCreateDTO planCreateDTO) {

        createAndUpdateValidations(
                planCreateDTO.getCaracteristicas(),
                planCreateDTO.getMiniaturaSelect(),
                planCreateDTO.getImagenesFiles().size());

        PlanExclusivo planExclusivo = PlanExclusivoCreateMapper.mapper.planExclusivoCreateDtoToPlanExclusivo(planCreateDTO);

        planExclusivo.addServiciosIncluidos(planCreateDTO.getServiciosIncluidos());
        planExclusivo.addInformacionAdicional(planCreateDTO.getInformacionAdicional());
        planExclusivo.addDisponibilidad(planCreateDTO.getDisponibilidad());
        planExclusivo.addCaracteristicas(planCreateDTO.getCaracteristicas().stream()
                .map(ic -> new Caracteristica(ic))
                .collect(Collectors.toList())
        );

        planExclusivo = planExclusivoService.createPlanExclusivo(planExclusivo);

        List<String> urls = storageService.savePlanImages(planExclusivo.getId(), planExclusivo.getNombre(), planCreateDTO.getImagenesFiles());

        planExclusivo.addImagenes(urls);
        planExclusivo.setImagenMiniatura(urls.get(planCreateDTO.getMiniaturaSelect()));

        planExclusivoService.createPlanExclusivo(planExclusivo);

        return new ResponseEntity<>(
                SuccessResponse
                        .builder()
                        .message("Exclusive plan created successfully")
                        .build(),
                HttpStatus.OK);
    }

    private void createAndUpdateValidations(List<Long> idCaracteristicasList, int miniaturaSelect, int imagesFilesSize) {
//      se valida que las caracteristicas que se estan relacionando al plan si existan en la base de datos
        caracteristicaService.isExistsCaracteristicas(idCaracteristicasList);

//      se valida que la miniatura seleccionada del plan no pueda ser mayor al numero de imagenes que vienen relacionadas al plan
//      o sea que si vienen 4 imagenes no pueda seleccionar como miniatura la imagen 5, porque no exite
        if (miniaturaSelect > (imagesFilesSize - 1)) {
            throw new BadRequestException(String
                    .format("MiniaturaSelect cannot be larger than the size of ImagenesFiles"));
        }

//      se valida que la miniatura seleccionada del plan no pueda se menor que 0
        if (miniaturaSelect < 0) {
            throw new BadRequestException(String
                    .format("MiniaturaSelect cannot be less than 0"));
        }
    }
}
