package application.costa_tour.service;

import application.costa_tour.model.Cliente;
import application.costa_tour.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente getClientByDni (String dni) {
        return clienteRepository.findById(dni).orElse(null);
    }
}
