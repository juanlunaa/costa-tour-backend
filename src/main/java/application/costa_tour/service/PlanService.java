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

    public Long createPlan (Plan plan) {
        return planRepository.save(plan).getId();
    }
}
