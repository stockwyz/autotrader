package com.mcwilliamauto.modules.lead.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LeadTestDriveDTO {
    private Long vehicleId;
    @NotBlank private String firstName;
    @NotBlank private String lastName;
    @NotBlank @Email private String email;
    @NotBlank private String phone;
    private String message;
    private LocalDateTime preferredTime;
}
