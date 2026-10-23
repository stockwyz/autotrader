package com.mcwilliamauto.modules.lead.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LeadInquiryDTO {
    private Long vehicleId;
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotBlank(message = "Email is required") @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Phone is required")
    private String phone;
    private String message;
    private String sourcePage;
}
