package application.costa_tour.service;

import application.costa_tour.dto.PlanDTO;
import application.costa_tour.dto.mapper.PlanMapper;
import application.costa_tour.exception.ResourceNotFoundException;
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

    public Plan getPlanEntity (Long id) {
        return planRepository.findById(id).orElse(null);
    }

    public Long createPlan (Plan plan) {
        return planRepository.save(plan).getId();
    }

    public void updatePlan (Long id, Plan plan) {
        Plan previousPlan = planRepository.findById(id).orElse(null);

        previousPlan.setNombre(plan.getNombre());
        previousPlan.setDescripcion(plan.getDescripcion());
        previousPlan.setCategoria(plan.getCategoria());
        previousPlan.setRangoPrecio(plan.getRangoPrecio());
        previousPlan.setUbicacion(plan.getUbicacion());
        previousPlan.setImagenCard(plan.getImagenCard());
        previousPlan.setImagenes(plan.getImagenes());

        planRepository.save(previousPlan);
    }

    public void deletePlan (Long id) {
        planRepository.deleteById(id);
    }
}
