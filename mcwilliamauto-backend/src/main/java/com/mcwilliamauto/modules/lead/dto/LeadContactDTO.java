package com.mcwilliamauto.modules.lead.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LeadContactDTO {
    private String type = "CONTACT"; // CONTACT / TEXT_US
    private String firstName;
    private String lastName;
    @NotBlank(message = "Email is required")
    private String email;
    private String phone;
    private String message;
}
