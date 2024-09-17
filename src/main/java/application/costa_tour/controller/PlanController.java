package application.costa_tour.controller;

import application.costa_tour.dto.PlanCreateDTO;
import application.costa_tour.dto.mapper.PlanCreateMapper;
import application.costa_tour.exception.SuccessResponse;
import application.costa_tour.model.Caracteristica;
import application.costa_tour.model.CaracteristicaPlan;
import application.costa_tour.model.Plan;
import application.costa_tour.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

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

    @PostMapping("/create")
    public ResponseEntity<?> createPlan (@ModelAttribute @Valid PlanCreateDTO planCreateDTO) {

        Plan plan = PlanCreateMapper.mapper.planCreateDtoToPlan(planCreateDTO);

        ubicacionService.createUbicacion(plan.getUbicacion());
        Long idPlan = planService.createPlan(plan);

        String rutaImagenes = storageService.savePlanImages(idPlan, planCreateDTO.getNombre(), planCreateDTO.getImagenesFiles());

        plan.setId(idPlan);
        plan.setImagenes(rutaImagenes);
        plan.setImagenCard(rutaImagenes.split(";")[planCreateDTO.getMiniaturaSelect()]);

        planService.createPlan(plan);

        List<CaracteristicaPlan> caracteristicasPlan = new ArrayList<>();

        planCreateDTO.getCaracteristicas().forEach(c -> {
            CaracteristicaPlan caracteristicaPlan = new CaracteristicaPlan();

            caracteristicaPlan.setPlan(plan);
            caracteristicaPlan.setCaracteristica(caracteristicaService.getCaracteristica(c));
            caracteristicasPlan.add(caracteristicaPlan);
        });

        caracteristicaPlanService.createAllCaracteristicasPlan(caracteristicasPlan);

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

        Plan plan = PlanCreateMapper.mapper.planCreateDtoToPlan(planCreateDTO);
        Plan prevPlan = planService.getPlanEntity(idPlan);
        String prevPlanName = prevPlan.getNombre();

        plan.getUbicacion().setId(prevPlan.getUbicacion().getId());
        planService.updatePlan(idPlan, plan);

        String rutaImagenes = storageService.updatePlanImages(idPlan, prevPlanName, plan.getNombre(), planCreateDTO.getImagenesFiles());

        plan.setImagenes(rutaImagenes);
        plan.setImagenCard(rutaImagenes.split(";")[planCreateDTO.getMiniaturaSelect()]);

        planService.updatePlan(idPlan, plan);

        return new ResponseEntity<>(
                SuccessResponse
                        .builder()
                        .message("Plan updated successfully")
                        .build(),
                HttpStatus.OK);
    }

    @DeleteMapping("/delete/{idPlan}")
    public ResponseEntity<?> deletePlan (@PathVariable("idPlan") Long idPlan) {

        String planName = planService.getPlan(idPlan).getNombre();
        caracteristicaPlanService.deleteAllCaracteristicasPlanFromPlan(idPlan);
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
