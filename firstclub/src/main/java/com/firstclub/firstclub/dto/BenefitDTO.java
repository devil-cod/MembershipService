package com.firstclub.firstclub.dto;

import com.firstclub.firstclub.model.Benefit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Benefit DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BenefitDTO {
    
    private Long id;
    
    private Long tierId;
    
    private String tierName;
    
    @NotBlank(message = "Benefit name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Benefit type is required")
    private Benefit.BenefitType benefitType;
    
    private String value;
    
    private Boolean active;
}
