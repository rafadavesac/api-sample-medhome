package br.edu.atitus.apisample.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

public record PointDTO(
        double latitude,
        double longitude,
        String patientName,
        String serviceType,
        LocalDate appointmentDate,
        LocalTime appointmentTime
) {
}
