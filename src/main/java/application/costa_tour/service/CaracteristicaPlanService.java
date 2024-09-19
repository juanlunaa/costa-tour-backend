package application.costa_tour.service;

import application.costa_tour.model.CaracteristicaPlan;
import application.costa_tour.repository.CaracteristicaPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaracteristicaPlanService {

    @Autowired
    private CaracteristicaPlanRepository caracteristicaPlanRepository;

    public List<CaracteristicaPlan> getCaracteristicasPlanByIdPlan(Long idPlan) {
        return caracteristicaPlanRepository.findCaracteristicaPlanListByPlanId(idPlan);
    }

    public void createAllCaracteristicasPlan (List<CaracteristicaPlan> caracteristicaPlanList) {
        caracteristicaPlanRepository.saveAll(caracteristicaPlanList);
    }

    public void updateCaracteristicasPlan (Long idPlan, List<CaracteristicaPlan> newCaracteristicaPlanList) {
        List<CaracteristicaPlan> prevCaracteristicaPlanList = caracteristicaPlanRepository.findCaracteristicaPlanListByPlanId(idPlan);


    }

    public void deleteAllCaracteristicasPlanFromPlan (Long idPlan) {
        caracteristicaPlanRepository.removeAllCaracteristicaPlanByPlan(idPlan);
    }
}
