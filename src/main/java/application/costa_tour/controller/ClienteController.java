package application.costa_tour.controller;

import application.costa_tour.dto.ClienteCreateDTO;
import application.costa_tour.dto.ClienteDTO;
import application.costa_tour.dto.mapper.ClienteCreateMapper;
import application.costa_tour.exception.ClientAlredyExistException;
import application.costa_tour.exception.ResourceNotFoundException;
import application.costa_tour.model.Cliente;
import application.costa_tour.model.UserRole;
import application.costa_tour.service.CiudadService;
import application.costa_tour.service.ClienteService;
import application.costa_tour.service.TipoDocumentoService;
import application.costa_tour.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@Validated
public class ClienteController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private TipoDocumentoService tipoDocumentoService;

    @Autowired
    private CiudadService ciudadService;

    @GetMapping
    public ResponseEntity<?> clientByDni (@RequestParam String dni) {
        ClienteDTO cliente = clienteService.getClientByDni(dni);
        return ResponseEntity.ok(cliente);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createClient (@ModelAttribute @Valid ClienteCreateDTO clienteDto) {

        if (clienteService.isExistingClient(clienteDto.getDni())) {
            throw new ClientAlredyExistException("Client already exist.");
        }

        if (!tipoDocumentoService.isExistDocType(clienteDto.getIdTipoDocumento())) {
            throw new ResourceNotFoundException("Doc type not found to id=" + clienteDto.getIdTipoDocumento());
        }

        if (!ciudadService.isExitsCity(clienteDto.getIdCiudad())) {
            throw new ResourceNotFoundException("City not found to id=" + clienteDto.getIdCiudad());
        }

        Cliente cliente = ClienteCreateMapper.mapper.clienteCreateDtoToCliente(clienteDto);

        cliente.getUsuario().setTipo(UserRole.CLIENTE);
        usuarioService.createUser(cliente.getUsuario());

        ClienteDTO clienteRes = clienteService.createClient(cliente);
        return new ResponseEntity<>(
                clienteRes,
                HttpStatus.CREATED
                );
    }
}
