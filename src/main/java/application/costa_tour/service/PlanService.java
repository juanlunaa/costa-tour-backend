package application.costa_tour.service;

import application.costa_tour.dto.CaracteristicaPlanDTO;
import application.costa_tour.dto.InteresTuristaDTO;
import application.costa_tour.dto.PlanDTO;
import application.costa_tour.dto.mapper.PlanMapper;
import application.costa_tour.exception.BadRequestException;
import application.costa_tour.exception.ResourceNotFoundException;
import application.costa_tour.model.Caracteristica;
import application.costa_tour.model.CaracteristicaPlan;
import application.costa_tour.model.ImagenPlan;
import application.costa_tour.model.Plan;
import application.costa_tour.model.enums.PlanCategory;
import application.costa_tour.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    public Plan getPlanEntityById (Long id) {
        return planRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String
                                .format("Plan not found for id=%s", id)));
    }

    public PlanDTO getPlan (Long id) {
        return PlanMapper.mapper.planToPlanDto(planRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(String
                                        .format("Plan not found for id=%s", id))));
    }

    public List<PlanDTO> getAllPlans () {
//        return PlanMapper.mapper.plansToPlanDtos(planRepository.findAll());
        return PlanMapper.mapper.plansToPlanDtos(planRepository.findAllPlansWithoutPlansExclusive());
    }

    public List<PlanDTO> getPlansByCategoria(PlanCategory categoria) {
        return PlanMapper.mapper.plansToPlanDtos(planRepository.findPlansByCategoria(categoria));
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

    public List<PlanDTO> planRecomendation(List<PlanDTO> planes, List<InteresTuristaDTO> interesesTurista, int numPlanes) {

        if (numPlanes > planes.size()) {
            throw new BadRequestException("The number of plans to recommend is greater than the total number of plans");
        }

        PriorityQueue<PlanDTO> neighbors = new PriorityQueue<>(
                Comparator.comparingDouble(p -> calculateDistanceBetweenPlanAndTurista(p.getCaracteristicas(), interesesTurista))
        );

        for (PlanDTO plan : planes) {
            neighbors.offer(plan);
        }

        List<PlanDTO> planesCercanosAlTurista = new ArrayList<>();

        for (int i = 0; i < numPlanes; i++) {
            planesCercanosAlTurista.add(neighbors.poll());
        }

        return planesCercanosAlTurista;
    }

    public List<PlanDTO> getPlansFavorites(String dniTurista) {
        return PlanMapper.mapper.plansToPlanDtos(
                planRepository.findPlansFavoritesByTuristaDni(dniTurista));
    }

    public double calculateDistanceBetweenPlanAndTurista(
            List<CaracteristicaPlanDTO> caracteristicasPlan,
            List<InteresTuristaDTO> interesTurista
    ) {
        Set<String> setIntereses = interesTurista.stream()
                .map(i -> i.getInteres())
                .collect(Collectors.toSet());

        Set<String> setCaracteristicas = caracteristicasPlan.stream()
                .map(c -> c.getCaracteristica())
                .collect(Collectors.toSet());

        setIntereses.retainAll(setCaracteristicas);
        int commonElements = setIntereses.size();

        return 1.0 / (1.0 + commonElements);
    }
}
