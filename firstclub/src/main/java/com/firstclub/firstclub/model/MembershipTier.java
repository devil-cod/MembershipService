package com.firstclub.firstclub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Tier entity for membership levels
 */
@Entity
@Table(name = "membership_tiers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipTier {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TierLevel tierLevel;
    
    @Column(name = "min_order_count")
    private Integer minOrderCount;
    
    @Column(name = "min_order_value")
    private Long minOrderValue;
    
    @Column(name = "discount_percentage")
    private Long discountPercentage;
    
    @Column(name = "free_delivery")
    private Boolean freeDelivery = false;
    
    @Column(name = "priority_support")
    private Boolean prioritySupport = false;
    
    @Column(name = "exclusive_deals")
    private Boolean exclusiveDeals = false;
    
    private String description;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum TierLevel {
        SILVER,
        GOLD,
        PLATINUM
    }
}
