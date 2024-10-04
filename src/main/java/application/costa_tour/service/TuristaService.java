package application.costa_tour.service;

import application.costa_tour.dto.TuristaDTO;
import application.costa_tour.dto.mapper.TuristaMapper;
import application.costa_tour.exception.ResourceNotFoundException;
import application.costa_tour.model.Interes;
import application.costa_tour.model.Turista;
import application.costa_tour.model.Usuario;
import application.costa_tour.repository.TuristaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class TuristaService {

    @Autowired
    private TuristaRepository turistaRepository;

    public TuristaDTO getTuristaByUser (Usuario user) {
        Turista turista = turistaRepository.findTuristaByUsuarioId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(String
                            .format("Tourist not found for user id=%s", user.getId())));

        return TuristaMapper.mapper.turistaToTuristaDto(turista);
    }

    public TuristaDTO getTuristaByDni (String dni) {
        Turista turista = turistaRepository.findById(dni)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String
                                .format("Tourist not found for dni=%s", dni)));

        return TuristaMapper.mapper.turistaToTuristaDto(turista);
    }

    public boolean isExistingTurista (String dni) {
        return turistaRepository.findById(dni).orElse(null) != null;
    }

    public boolean macthEmailToken(String dni, String emailToken) {
        String emailUser = turistaRepository.findEmailUsuarioByTuristaDni(dni)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String
                                .format("Turist not found for dni=%s", dni)));

        return emailUser.equals(emailToken);
    }

    public TuristaDTO createTurista (Turista turista) {
        turista.setEdad(calculateAge(turista.getFechaNacimiento()));

        turistaRepository.save(turista);

        turista = turistaRepository.findById(turista.getDni()).orElse(null);

        return TuristaMapper.mapper.turistaToTuristaDto(turista);
    }

    public TuristaDTO updateTurista(String dni, Turista turista, List<Interes> newIntereses) {
        Turista previousTurista = turistaRepository.findById(dni).orElse(null);

        previousTurista.setNombre(turista.getNombre());
        previousTurista.setApellido(turista.getApellido());
        previousTurista.setFechaNacimiento(turista.getFechaNacimiento());
        previousTurista.setEdad(calculateAge(turista.getFechaNacimiento()));
        previousTurista.setCiudad(turista.getCiudad());

        previousTurista.updateIntereses(newIntereses);
        turistaRepository.save(previousTurista);
        Turista turistaUpdated = turistaRepository.findById(dni).orElse(null);

        return TuristaMapper.mapper.turistaToTuristaDto(turistaUpdated);
    }

    private int calculateAge (Date fechaNacimiento) {
        LocalDate birthday = fechaNacimiento
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        return Period
                .between(birthday, LocalDate.now())
                .getYears();
    }
}
