package application.costa_tour.service;

import application.costa_tour.dto.CodigoPlanDTO;
import application.costa_tour.dto.mapper.CodigoPlanMapper;
import application.costa_tour.exception.BadRequestException;
import application.costa_tour.exception.ResourceNotFoundException;
import application.costa_tour.model.CodigoPlan;
import application.costa_tour.model.Plan;
import application.costa_tour.model.Turista;
import application.costa_tour.model.enums.CodePlanActions;
import application.costa_tour.model.enums.CodePlanStatus;
import application.costa_tour.repository.CodigoPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CodigoPlanService {

    @Autowired
    private CodigoPlanRepository codigoPlanRepository;

    @Autowired
    private CodigoPlanMapper codigoPlanMapper;

    public List<CodigoPlanDTO> getAllCodigosByTuristDni(String dniTurist) {
        return codigoPlanMapper.codigoPlansToCodigoPlanDtos(codigoPlanRepository.findByTuristaDni(dniTurist));
    }

    public CodigoPlanDTO getCodigoByTuristaAndPlan(Turista turista, Long planId) {
        CodigoPlan codigoPlan = codigoPlanRepository.findByTuristaDniAndPlanIdAndEstado(turista.getDni(), planId, CodePlanStatus.GENERADO)
                .orElse(null);
        return codigoPlan != null && codigoPlan.getEstado() == CodePlanStatus.GENERADO ? codigoPlanMapper.codigoPlanToCodigoPlanDto(codigoPlan) : null;
    }

    public CodigoPlanDTO generateCodigo(Turista turista, Plan plan) {
        CodigoPlan codigoPlan = new CodigoPlan();
        codigoPlan.setTurista(turista);
        codigoPlan.setPlan(plan);
        codigoPlan.setFechaGeneracion(LocalDateTime.now());
        codigoPlan.setEstado(CodePlanStatus.GENERADO);
        codigoPlan.setCodigo(generateCodigoUnico());

        codigoPlan = codigoPlanRepository.save(codigoPlan);

        return codigoPlanMapper.codigoPlanToCodigoPlanDto(codigoPlan);
    }

    public String processCodigo(String codigo, CodePlanActions accion) {
        CodigoPlan codigoPlan = codigoPlanRepository.findByCodigo(codigo)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Code plan not found for code=" + codigo));

        if (codigoPlan.getEstado() != CodePlanStatus.GENERADO) {
            throw new BadRequestException("Code plan already used or invalid");
        }
        System.out.println("accion = " + accion);
        if (accion == CodePlanActions.CANCELAR) {
            codigoPlan.setEstado(CodePlanStatus.CANCELADO);

            codigoPlanRepository.save(codigoPlan);
            return "Code plan canceled successfully";
        }

        codigoPlan.setEstado(CodePlanStatus.VALIDADO);
        codigoPlan.setFechaUso(LocalDateTime.now());

        codigoPlanRepository.save(codigoPlan);
        return "Code plan used successfully";
    }

    private String generateCodigoUnico() {
        String codigo;
        do {
            codigo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (codigoPlanRepository.existsByCodigo(codigo));

        return codigo;
    }
}
