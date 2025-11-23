package com.firstclub.firstclub.dto;

import com.firstclub.firstclub.model.Subscription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Subscription DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDTO {
    
    private Long id;
    
    private Long userId;
    
    private String userName;
    
    private String userEmail;
    
    private Long planId;
    
    private String planName;
    
    private Long tierId;
    
    private String tierName;
    
    private Subscription.SubscriptionStatus status;
    
    private LocalDateTime startDate;
    
    private LocalDateTime endDate;
    
    private Boolean autoRenew;
}
