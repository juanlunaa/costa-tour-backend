package application.costa_tour.service;

import application.costa_tour.dto.TuristaDTO;
import application.costa_tour.dto.mapper.TuristaMapper;
import application.costa_tour.exception.ResourceNotFoundException;
import application.costa_tour.model.Turista;
import application.costa_tour.model.Usuario;
import application.costa_tour.repository.TuristaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

@Service
public class TuristaService {

    @Autowired
    private TuristaRepository turistaRepository;

    public TuristaDTO getTuristaByUser (Usuario user) {
        Turista turista = turistaRepository.findTuristaByUsuarioId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(String
                            .format("Client not found for user id=%s", user.getId())));

        return TuristaMapper.mapper.turistaToTuristaDto(turista);
    }

    public TuristaDTO getTuristaByDni (String dni) {
        Turista turista = turistaRepository.findById(dni).orElse(null);
        return TuristaMapper.mapper.turistaToTuristaDto(turista);
    }

    public TuristaDTO createTurista (Turista turista) {
        LocalDate birthday = turista
                .getFechaNacimiento()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        turista.setEdad(calculateAge(birthday));

        turistaRepository.save(turista);

        turista = turistaRepository.findById(turista.getDni()).orElse(null);

        return TuristaMapper.mapper.turistaToTuristaDto(turista);
    }

    public boolean isExistingTurista (String dni) {
        return turistaRepository.findById(dni).orElse(null) != null;
    }

    private int calculateAge (LocalDate birthday) {
        return Period
                .between(birthday, LocalDate.now())
                .getYears();
    }
}
