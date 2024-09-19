package application.costa_tour.controller;

import application.costa_tour.dto.PlanCreateDTO;
import application.costa_tour.dto.mapper.PlanCreateMapper;
import application.costa_tour.exception.ResourceNotFoundException;
import application.costa_tour.exception.SuccessResponse;
import application.costa_tour.model.*;
import application.costa_tour.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/plan")
public class PlanController {

    @Autowired
    private PlanService planService;

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

        Plan plan = PlanCreateMapper.mapper.planCreateDtoToPlan(planCreateDTO);

        List<CaracteristicaPlan> caracteristicasPlan = planCreateDTO.getCaracteristicas()
                .stream()
                .map(c -> new CaracteristicaPlan(
                            plan,
                            new Caracteristica(c)))
                .collect(Collectors.toList());

        plan.setCaracteristicasPlan(caracteristicasPlan);

        ubicacionService.createUbicacion(plan.getUbicacion());

        Plan newPlan = planService.createPlan(plan);

        List<String> urls = storageService.savePlanImages(newPlan.getId(), planCreateDTO.getNombre(), planCreateDTO.getImagenesFiles());
        List<ImagenPlan> imagenPlanList = urls.stream()
                .map(url -> new ImagenPlan(url))
                .collect(Collectors.toList());

        newPlan.setImagenes(imagenPlanList);
        newPlan.setImagenMiniatura(urls.get(planCreateDTO.getMiniaturaSelect()));

        planService.createPlan(newPlan);

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

        Plan plan = PlanCreateMapper.mapper.planCreateDtoToPlan(planCreateDTO);
        String prevPlanName = planService.getNombrePlan(idPlan);

        List<Caracteristica> caracteristicas = planCreateDTO.getCaracteristicas().stream()
                .map(c -> new Caracteristica(c))
                .collect(Collectors.toList());

        List<String> urls = storageService.updatePlanImages(idPlan, prevPlanName, plan.getNombre(), planCreateDTO.getImagenesFiles());

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
}
