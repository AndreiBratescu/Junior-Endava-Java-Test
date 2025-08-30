package com.example.carins.web.dto;

public record InsuranceValidityResponseDto(Long carId, String date, boolean valid) {}