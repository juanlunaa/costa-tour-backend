package application.costa_tour.controller;

import application.costa_tour.dto.PlanCreateDTO;
import application.costa_tour.dto.mapper.PlanCreateMapper;
import application.costa_tour.exception.SuccessResponse;
import application.costa_tour.model.Plan;
import application.costa_tour.service.PlanService;
import application.costa_tour.service.StorageService;
import application.costa_tour.service.UbicacionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/plan")
public class PlanController {

    @Autowired
    private PlanService planService;

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

        return new ResponseEntity<>(
                SuccessResponse
                        .builder()
                        .message("Plan created successfully")
                        .build(),
                HttpStatus.OK);
    }
}
