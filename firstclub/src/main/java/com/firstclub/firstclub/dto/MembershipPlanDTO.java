package com.firstclub.firstclub.dto;

import com.firstclub.firstclub.model.MembershipPlan;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Plan DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipPlanDTO {
    
    private Long id;
    
    @NotBlank(message = "Plan name is required")
    private String name;
    
    @NotNull(message = "Plan type is required")
    private MembershipPlan.PlanType planType;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Long price;
    
    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    private Integer durationInDays;
    
    private String description;
    
    private Boolean active;
}
