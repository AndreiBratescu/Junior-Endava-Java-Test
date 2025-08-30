package com.example.carins.web.dto;

import java.time.LocalDate;

public record ClaimDto(
        LocalDate claimDate,
        String description,
        Double amount
) {}