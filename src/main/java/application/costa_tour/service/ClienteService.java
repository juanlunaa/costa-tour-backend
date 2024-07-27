package application.costa_tour.service;

import application.costa_tour.dto.ClienteCreateDTO;
import application.costa_tour.dto.ClienteDTO;
import application.costa_tour.dto.mapper.ClienteCreateMapper;
import application.costa_tour.dto.mapper.ClienteMapper;
import application.costa_tour.model.Cliente;
import application.costa_tour.repository.ClienteRepository;
import application.costa_tour.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private StorageService storageService;

    @Value("${uploadfiles.avatar.location}")
    private String filesLocation;

    public ClienteDTO getClientByDni (String dni) {
        Cliente cliente = clienteRepository.findById(dni).orElse(null);
        return ClienteMapper.mapper.clienteToClienteDto(cliente);
    }

    public void createClient (ClienteCreateDTO clienteDto) {
        Cliente cliente = ClienteCreateMapper.mapper.clienteCreateDtoToCliente(clienteDto);
        System.out.println(cliente.getTipoDocumento().getDescripcion());

        cliente.getUsuario().setTipo("Cliente");

        LocalDate birthday = cliente
                .getFechaNacimiento()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        cliente.setEdad(calculateAge(birthday));
        cliente.getUsuario().setImagenPerfil("url-client-avatar");

        usuarioRepository.save(cliente.getUsuario());

        clienteRepository.save(cliente);
    }

    public void uploadAvatar (String dniCliente, MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed saving, file is empty");
        }

        Cliente cliente = clienteRepository.findById(dniCliente).orElse(null);

        if (cliente == null) {
            throw new RuntimeException("Client not found");
        }

        String filename = String
                .format("%s-avatar%s", dniCliente, getFileExtension(file.getOriginalFilename()));

        storageService.saveFile(filesLocation, file, filename);

        cliente.getUsuario().setImagenPerfil(filename);

        usuarioRepository.save(cliente.getUsuario());
    }

    private int calculateAge (LocalDate birthday) {
        return Period
                .between(birthday, LocalDate.now())
                .getYears();
    }

    private String getFileExtension (String filename) {
        String fileExtension = "";
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return fileExtension = filename.substring(dotIndex);
        }
        return ".jpg";
    }
}
