package com.mcwilliamauto.modules.appointment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentDTO {
    @NotBlank
    @Pattern(regexp = "REPAIR|MAINTENANCE|PHR", message = "Invalid service type")
    private String serviceType;
    @NotNull private LocalDate apptDate;
    @NotNull private LocalTime apptTime;
    @Size(max = 2000) private String detail;
    @NotBlank @Size(max = 100) private String customerName;
    @NotBlank @Size(max = 30) private String phone;
    @Email @Size(max = 100) private String email;
    @Size(max = 150) private String vehicleInfo;
}
