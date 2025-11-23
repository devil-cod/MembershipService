package com.firstclub.firstclub.dto;

import com.firstclub.firstclub.model.MembershipTier;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Tier DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipTierDTO {
    
    private Long id;
    
    @NotBlank(message = "Tier name is required")
    private String name;
    
    @NotNull(message = "Tier level is required")
    private MembershipTier.TierLevel tierLevel;
    
    private Integer minOrderCount;
    
    private Long minOrderValue;
    
    private Long discountPercentage;
    
    private Boolean freeDelivery;
    
    private Boolean prioritySupport;
    
    private Boolean exclusiveDeals;
    
    private String description;
    
    private Boolean active;
}
