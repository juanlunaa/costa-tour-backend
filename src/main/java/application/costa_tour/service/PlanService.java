package application.costa_tour.service;

import application.costa_tour.dto.PlanDTO;
import application.costa_tour.dto.mapper.PlanMapper;
import application.costa_tour.exception.ResourceNotFoundException;
import application.costa_tour.model.Caracteristica;
import application.costa_tour.model.CaracteristicaPlan;
import application.costa_tour.model.ImagenPlan;
import application.costa_tour.model.Plan;
import application.costa_tour.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    public PlanDTO getPlan (Long id) {
        return PlanMapper.mapper.planToPlanDto(planRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(String
                                        .format("Plan not found for id=%s", id))));
    }

    public List<PlanDTO> getAllPlans () {
        return PlanMapper.mapper.plansToPlanDtos(planRepository.findAll());
    }

    public Plan createPlan (Plan plan) {
        return planRepository.save(plan);
    }

    public void updatePlan (Long id, Plan plan, List<Caracteristica> newCaracteristicasPlan, List<String> newImagenesPlan) {
        Plan previousPlan = planRepository.findById(id).orElse(null);

        previousPlan.setNombre(plan.getNombre());
        previousPlan.setDescripcion(plan.getDescripcion());
        previousPlan.setCategoria(plan.getCategoria());
        previousPlan.setRangoMinDinero(plan.getRangoMinDinero());
        previousPlan.setRangoMaxDinero(plan.getRangoMaxDinero());

        previousPlan.getUbicacion().setLatitud(plan.getUbicacion().getLatitud());
        previousPlan.getUbicacion().setLongitud(plan.getUbicacion().getLongitud());
        previousPlan.getUbicacion().setDireccion(plan.getUbicacion().getDireccion());

        previousPlan.setImagenMiniatura(plan.getImagenMiniatura());
        previousPlan.updateCaracteristicas(newCaracteristicasPlan);
        previousPlan.updateImagenesPlan(newImagenesPlan);

        planRepository.save(previousPlan);
    }

    public void deletePlan (Long id) {
        planRepository.deleteById(id);
    }

    public String getNombrePlan(Long id) {
        return planRepository.findPlanNameByIdPlan(id);
    }

    public boolean isPlanExists(Long id) {
        return planRepository.findById(id).orElse(null) != null;
    }
}
