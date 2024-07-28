package application.costa_tour.service;

import application.costa_tour.dto.ClienteDTO;
import application.costa_tour.dto.mapper.ClienteMapper;
import application.costa_tour.model.Cliente;
import application.costa_tour.repository.ClienteRepository;
import application.costa_tour.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private StorageService storageService;

    public ClienteDTO getClientByDni (String dni) {
        Cliente cliente = clienteRepository.findById(dni).orElse(null);
        return ClienteMapper.mapper.clienteToClienteDto(cliente);
    }

    public ClienteDTO createClient (Cliente cliente) {
        LocalDate birthday = cliente
                .getFechaNacimiento()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        cliente.setEdad(calculateAge(birthday));

        clienteRepository.save(cliente);

        cliente = clienteRepository.findById(cliente.getDni()).orElse(null);

        return ClienteMapper.mapper.clienteToClienteDto(cliente);
    }

    public boolean isExistingClient (String dni) {
        return clienteRepository.findById(dni).orElse(null) != null;
    }

    private int calculateAge (LocalDate birthday) {
        return Period
                .between(birthday, LocalDate.now())
                .getYears();
    }
}
