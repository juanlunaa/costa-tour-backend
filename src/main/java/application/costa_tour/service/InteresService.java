package application.costa_tour.service;

import application.costa_tour.dto.InteresDTO;
import application.costa_tour.dto.mapper.InteresMapper;
import application.costa_tour.exception.ResourceNotFoundException;
import application.costa_tour.model.Interes;
import application.costa_tour.repository.InteresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InteresService {

    @Autowired
    private InteresRepository interesRepository;

    public List<InteresDTO> getAllIntereses() {
        return InteresMapper.mapper.interesesToInteresesDtos(interesRepository.findAll());
    }

    public void isExistsIntereses(List<Long> idIntereses) {
        Map<Long, Interes> interesesMap = interesRepository.findAll().stream()
                .collect(Collectors.toMap(Interes::getId, i -> i));

        for (Long id : idIntereses) {
            if (!interesesMap.containsKey(id)) {
                throw new ResourceNotFoundException(String
                        .format("Interes not found for id=%s", id));
            }
        }
    }
}
