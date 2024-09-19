package application.costa_tour.controller;

import application.costa_tour.dto.PlanCreateDTO;
import application.costa_tour.dto.mapper.PlanCreateMapper;
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

        Plan plan = PlanCreateMapper.mapper.planCreateDtoToPlan(planCreateDTO);
        Plan prevPlan = planService.getPlanEntity(idPlan);
        String prevPlanName = prevPlan.getNombre();

        prevPlan.setNombre(planCreateDTO.getNombre());
        prevPlan.setDescripcion(planCreateDTO.getDescripcion());
        prevPlan.setCategoria(planCreateDTO.getCategoria());
        prevPlan.setRangoMinDinero(planCreateDTO.getRangoMinDinero());
        prevPlan.setRangoMaxDinero(planCreateDTO.getRangoMaxDinero());

        prevPlan.getUbicacion().setLatitud(planCreateDTO.getLatitud());
        prevPlan.getUbicacion().setLongitud(planCreateDTO.getLongitud());
        prevPlan.getUbicacion().setDireccion(planCreateDTO.getDireccion());

        // falta añadir que compare las caracteristicas nuevas con las que estaban antes
        // para no estar consumiendo ids de la bd innecesariamente
        prevPlan.getCaracteristicasPlan().clear();
        planCreateDTO.getCaracteristicas()
                .forEach(c -> {
                    prevPlan.getCaracteristicasPlan().add(new CaracteristicaPlan(
                            prevPlan,
                            new Caracteristica(c)
                    ));
                });

        // lo mismo de las caracteristicas añadirlo aca
        prevPlan.getImagenes().clear();

        List<String> urls = storageService.updatePlanImages(idPlan, prevPlanName, plan.getNombre(), planCreateDTO.getImagenesFiles());
        urls.forEach(url -> {
            prevPlan.getImagenes().add(new ImagenPlan(url));
        });

        prevPlan.setImagenMiniatura(urls.get(planCreateDTO.getMiniaturaSelect()));

        planService.updatePlan(idPlan, prevPlan);

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
