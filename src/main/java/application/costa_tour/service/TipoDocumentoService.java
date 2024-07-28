package application.costa_tour.service;

import application.costa_tour.repository.TipoDocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipoDocumentoService {

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    public boolean isExistDocType (Long id) {
        return tipoDocumentoRepository.existsById(id);
    }
}
