package application.costa_tour.service;

import application.costa_tour.dto.CiudadDTO;
import application.costa_tour.dto.EstadoDTO;
import application.costa_tour.dto.PaisDTO;
import application.costa_tour.dto.mapper.CiudadMapper;
import application.costa_tour.dto.mapper.EstadoMapper;
import application.costa_tour.dto.mapper.PaisMapper;
import application.costa_tour.repository.CiudadRepository;
import application.costa_tour.repository.EstadoRepository;
import application.costa_tour.repository.PaisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    PaisRepository paisRepository;

    @Autowired
    EstadoRepository estadoRepository;

    @Autowired
    CiudadRepository ciudadRepository;

    public List<PaisDTO> getAllPaises() {
        return PaisMapper.mapper.paisesToPaisesDto(paisRepository.findAllOrder());
    }

    public List<EstadoDTO> getAllEstadosByPais(Long idPais) {
        return EstadoMapper.mapper.estadosToEstadosDto(estadoRepository.findEstadosByPaisId(idPais));
    }

    public List<CiudadDTO> getAllCiudadesByEstado(Long idEstado) {
        return CiudadMapper.mapper.ciudadesToCiudadesDto(ciudadRepository.findCiudadesByEstadoId(idEstado));
    }
}
