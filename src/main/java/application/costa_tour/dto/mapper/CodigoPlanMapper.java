package application.costa_tour.dto.mapper;

import application.costa_tour.dto.CodigoPlanDTO;
import application.costa_tour.model.CodigoPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TuristaMapper.class, PlanMapper.class})
public interface CodigoPlanMapper {

    List<CodigoPlanDTO> codigoPlansToCodigoPlanDtos(List<CodigoPlan> codigoPlan);

    @Mapping(source = "turista", target = "turista")
    @Mapping(source = "plan", target = "plan")
    CodigoPlanDTO codigoPlanToCodigoPlanDto(CodigoPlan codigoPlan);
}
