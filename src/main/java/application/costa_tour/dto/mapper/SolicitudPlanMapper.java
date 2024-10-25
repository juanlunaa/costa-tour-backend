package application.costa_tour.dto.mapper;

import application.costa_tour.dto.SolicitudPlanDTO;
import application.costa_tour.model.SolicitudPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {AliadoMapper.class, AdministradorMapper.class, PlanMapper.class})
public interface SolicitudPlanMapper {

    @Mapping(source = "aliado", target = "aliado")
    @Mapping(source = "administrador", target = "administrador")
    @Mapping(source = "plan", target = "plan")
    @Mapping(source = "id", target = "id")
    SolicitudPlanDTO solicitudPlanToSolicitudPlanDto(SolicitudPlan solicitudPlan);
}
