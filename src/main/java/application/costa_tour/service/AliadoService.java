package application.costa_tour.service;

import application.costa_tour.dto.AliadoDTO;
import application.costa_tour.dto.mapper.AliadoMapper;
import application.costa_tour.exception.ResourceNotFoundException;
import application.costa_tour.model.Aliado;
import application.costa_tour.model.Usuario;
import application.costa_tour.repository.AliadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AliadoService {

    @Autowired
    private AliadoRepository aliadoRepository;

    public Aliado getAliadoEntityByNit(String nitAliado) {
        return aliadoRepository.findById(nitAliado)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String
                                .format("Aliado not found for nit=%s", nitAliado)));
    }

    public AliadoDTO getAliadoByUser(Usuario user) {
        Aliado aliado = aliadoRepository.findAliadoByUsuarioId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(String
                                .format("Aliado not found for user id=%s", user.getId())));

        return AliadoMapper.mapper.aliadoToAliadoDto(aliado);
    }

    public AliadoDTO getAliadoByNit(String nitAliado) {
        Aliado aliado = aliadoRepository.findById(nitAliado).orElse(null);
        return AliadoMapper.mapper.aliadoToAliadoDto(aliado);
    }

    public AliadoDTO createAliado(Aliado aliado) {
        aliado = aliadoRepository.save(aliado);

        return AliadoMapper.mapper.aliadoToAliadoDto(aliado);
    }
}
