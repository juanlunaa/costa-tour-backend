package application.costa_tour.controller;

import application.costa_tour.exception.ResourceNotFoundException;
import application.costa_tour.model.Plan;
import application.costa_tour.model.Turista;
import application.costa_tour.model.enums.CodePlanActions;
import application.costa_tour.service.CodigoPlanService;
import application.costa_tour.service.PlanService;
import application.costa_tour.service.TuristaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/code-plan")
public class CodigoPlanController {

    @Autowired
    private CodigoPlanService codigoPlanService;

    @Autowired
    private TuristaService turistaService;

    @Autowired
    private PlanService planService;

    @GetMapping("/turist-codes/{dni}")
    private ResponseEntity<?> getCodigosByTurista(@PathVariable("dni") String dniTurista) {
        if (!turistaService.isExistingTurista(dniTurista)) {
            throw new ResourceNotFoundException("Turist not found for dni=" + dniTurista);
        }

        return ResponseEntity.ok(codigoPlanService.getAllCodigosByTuristDni(dniTurista));
    }

    @PostMapping("/create")
    private ResponseEntity<?> createCodigoPlan(@RequestParam String dniTurista, @RequestParam Long planId) {
        Plan plan = planService.getPlanEntityById(planId);
        Turista turista = turistaService.getTuristaEntityByDni(dniTurista);
        return ResponseEntity.ok(codigoPlanService.generateCodigo(turista, plan));
    }

    @PostMapping("/process")
    private ResponseEntity<?> processCodigoPlan(@RequestParam String codigo, @RequestParam CodePlanActions accion) {
        return ResponseEntity.ok(codigoPlanService.processCodigo(codigo, accion));
    }
}
