package com.example.carins.service;

import com.example.carins.model.Car;
import com.example.carins.model.Claim;
import com.example.carins.model.HistoryEvent;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.web.dto.ClaimDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final InsurancePolicyRepository policyRepository;

    public CarService(CarRepository carRepository, InsurancePolicyRepository policyRepository) {
        this.carRepository = carRepository;
        this.policyRepository = policyRepository;
    }

    public List<Car> listCars() {
        return carRepository.findAll();
    }

    public boolean isInsuranceValid(Long carId, LocalDate date) {
        if (carId == null || date == null) return false;
        // TODO: optionally throw NotFound if car does not exist
        return policyRepository.existsActiveOnDate(carId, date);
    }
    public Claim createClaim(Car car, ClaimDto claimDto) {
        Claim claim = new Claim();
        claim.setCar(car);
        claim.setClaimDate(claimDto.claimDate());
        claim.setDescription(claimDto.description());
        claim.setAmount(claimDto.amount());

        claim.setId(12345L);

        return claim;
    }

    public List<HistoryEvent> getCarHistory(Integer carId) {
        Car car = listCars().get(carId);

        if (car == null) {
            return List.of();
        }

        List<HistoryEvent> history = List.of(
                // Claims
                new HistoryEvent("Claim", LocalDate.of(2025, 8, 30), "Accident on highway, damage to front bumper."),
                new HistoryEvent("Claim", LocalDate.of(2023, 5, 12), "Rear-end collision, damage to tail light."),

                // Ownership Change
                new HistoryEvent("Ownership Change", LocalDate.of(2021, 3, 15), "Sold to new owner."),

                // Other Events
                new HistoryEvent("Insurance Update", LocalDate.of(2024, 2, 1), "Insurance renewed for 1 year.")
        );

        // Sort the events by date
        return history.stream()
                .sorted((e1, e2) -> e1.getEventDate().compareTo(e2.getEventDate()))
                .collect(Collectors.toList());
    }
}
