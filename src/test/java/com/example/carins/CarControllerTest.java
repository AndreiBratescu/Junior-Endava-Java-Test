package com.example.carins;

import com.example.carins.service.CarService;
import com.example.carins.web.CarController;
import com.example.carins.web.dto.InsuranceValidityResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CarControllerTest {

    @InjectMocks
    private CarController carController;

    @Mock
    private CarService carService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsInsuranceValid_CarNotFound() {
        Long carId = 1L;
        String date = "2025-08-30";

        when(carService.listCars().get(Math.toIntExact(carId))).thenReturn(false);

        ResponseEntity<?> response = carController.isInsuranceValid(Math.toIntExact(carId), date);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testIsInsuranceValid_InvalidDateFormat() {
        Long carId = 1L;
        String date = "2025-30-08";

        when(carService.listCars().get(Math.toIntExact(carId))).thenReturn(true);

        ResponseEntity<?> response = carController.isInsuranceValid(Math.toIntExact(carId), date);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Invalid date format"));
    }

    @Test
    void testIsInsuranceValid_DateOutOfRange() {
        Long carId = 1L;
        String date = "1920-08-30";

        when(carService.listCars().get(carId)).thenReturn(true);

        ResponseEntity<?> response = carController.isInsuranceValid(Math.toIntExact(carId), date);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Date is out of range"));
    }

    @Test
    void testIsInsuranceValid_Success() {
        Long carId = 1L;
        String date = "2025-08-30";

        when(carService.listCars().get(carId)).thenReturn(true);
        when(carService.isInsuranceValid(carId, LocalDate.parse(date))).thenReturn(true);

        ResponseEntity<?> response = carController.isInsuranceValid(Math.toIntExact(carId), date);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        CarController.InsuranceValidityResponse body = (CarController.InsuranceValidityResponse) response.getBody();
        assertNotNull(body);
        assertEquals(carId, body.carId());
        assertEquals(date, body.date());
        assertTrue(body.valid());
    }
}