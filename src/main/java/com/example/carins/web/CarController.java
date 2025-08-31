package com.example.carins.web;

import com.example.carins.model.Car;
import com.example.carins.model.Claim;
import com.example.carins.service.CarService;
import com.example.carins.web.dto.CarDto;
import com.example.carins.web.dto.ClaimDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CarController {

    private final CarService service;
    private ClaimDto claimDto;

    public CarController(CarService service) {
        this.service = service;
    }

    @GetMapping("/cars")
    public List<CarDto> getCars() {
        return service.listCars().stream().map(this::toDto).toList();
    }

    public ResponseEntity<?> isInsuranceValid(@PathVariable Integer carId, @RequestParam String date) {
        // Validate carId exists
        if (service.listCars().get(carId) == null) {
            return ResponseEntity.notFound().build();
        }

        // Validate date format
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format. Please use ISO format (YYYY-MM-DD).");
        }

        // Validate that the date is within a reasonable range
        if (parsedDate.isBefore(LocalDate.now().minusYears(100)) || parsedDate.isAfter(LocalDate.now().plusYears(100))) {
            return ResponseEntity.badRequest().body("Date is out of range. Please use a date within the past 100 years or the next 100 years.");
        }

        // Check insurance validity
        boolean valid = service.isInsuranceValid(Long.valueOf(carId), parsedDate);
        return ResponseEntity.ok(new InsuranceValidityResponse(carId, parsedDate.toString(), valid));
    }

    @PostMapping("/cars/{carId}/claims")
    public ResponseEntity<Claim> registerClaim(@PathVariable Integer carId, @Valid @RequestBody CarDto carDto) {

        Car car = service.listCars().get(carId);

        if (car == null) {
            return ResponseEntity.notFound().build();
        }

        Claim claim = service.createClaim(car, claimDto);

        return ResponseEntity.created(URI.create("/api/cars/" + carId + "/claims/" + claim.getId()))
                .body(claim);
    }

    private CarDto toDto(Car c) {
        var o = c.getOwner();
        return new CarDto(c.getId(), c.getVin(), c.getMake(), c.getModel(), c.getYearOfManufacture(),
                o != null ? o.getId() : null,
                o != null ? o.getName() : null,
                o != null ? o.getEmail() : null);
    }

    public record InsuranceValidityResponse(Integer carId, String date, boolean valid) {}
}
