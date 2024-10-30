package application.costa_tour.service;

import application.costa_tour.dto.mapper.plan.PlanExclusivoMapper;
import application.costa_tour.dto.plan.PlanExclusivoDTO;
import application.costa_tour.model.PlanExclusivo;
import application.costa_tour.repository.PlanExclusivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanExclusivoService {

    @Autowired
    private PlanExclusivoRepository planExclusivoRepository;

    public PlanExclusivoDTO getPlanExclusivoById(Long id) {
        return PlanExclusivoMapper.mapper.planExclusivoToPlanExclusivoDto(planExclusivoRepository.findById(id).orElse(null));
    }

    public List<PlanExclusivoDTO> getAllPlanExclusivo() {
        return PlanExclusivoMapper.mapper.planExclusivosToPlanExclusivoDtos(planExclusivoRepository.findAll());
    }

    public PlanExclusivo createPlanExclusivo(PlanExclusivo planExclusivo) {
        return planExclusivoRepository.save(planExclusivo);
    }
}
