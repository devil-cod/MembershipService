package com.firstclub.firstclub.service;

import com.firstclub.firstclub.dto.SubscriptionDTO;
import com.firstclub.firstclub.dto.SubscriptionRequest;
import com.firstclub.firstclub.model.*;
import com.firstclub.firstclub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Subscription-related business logic.
 */
@Service
@RequiredArgsConstructor
public class SubscriptionService {
    
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final MembershipPlanRepository planRepository;
    private final MembershipTierRepository tierRepository;
    private final MembershipTierService tierService;
    
    /**
     * Create a new subscription for a user.
     * 
     * @param request the subscription request
     * @return created subscription DTO
     */
    @Transactional
    public SubscriptionDTO createSubscription(SubscriptionRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));
        
        MembershipPlan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + request.getPlanId()));
        
        if (!plan.getActive()) {
            throw new RuntimeException("Plan is not active");
        }
        
        // Check if user already has an active subscription
        subscriptionRepository.findByUserIdAndStatus(user.getId(), Subscription.SubscriptionStatus.ACTIVE)
                .ifPresent(s -> {
                    throw new RuntimeException("User already has an active subscription");
                });
        
        // Calculate eligible tier based on user's order history
        MembershipTier tier = calculateUserTier(user);
        
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setPlan(plan);
        subscription.setTier(tier);
        subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(LocalDateTime.now().plusDays(plan.getDurationInDays()));
        subscription.setAutoRenew(request.getAutoRenew());
        
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return convertToDTO(savedSubscription);
    }
    
    /**
     * Get subscription by ID.
     * 
     * @param id the subscription ID
     * @return subscription DTO
     */
    public SubscriptionDTO getSubscriptionById(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + id));
        return convertToDTO(subscription);
    }
    
    /**
     * Get all subscriptions for a user.
     * 
     * @param userId the user ID
     * @return list of subscription DTOs
     */
    public List<SubscriptionDTO> getUserSubscriptions(Long userId) {
        return subscriptionRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get active subscription for a user.
     * 
     * @param userId the user ID
     * @return active subscription DTO
     */
    public SubscriptionDTO getActiveSubscription(Long userId) {
        Subscription subscription = subscriptionRepository
                .findByUserIdAndStatus(userId, Subscription.SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active subscription found for user"));
        return convertToDTO(subscription);
    }
    
    /**
     * Get all subscriptions.
     * 
     * @return list of subscription DTOs
     */
    public List<SubscriptionDTO> getAllSubscriptions() {
        return subscriptionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Upgrade subscription tier for a user.
     * 
     * @param subscriptionId the subscription ID
     * @return updated subscription DTO
     */
    @Transactional
    public SubscriptionDTO upgradeTier(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + subscriptionId));
        
        if (subscription.getStatus() != Subscription.SubscriptionStatus.ACTIVE) {
            throw new RuntimeException("Cannot upgrade inactive subscription");
        }
        
        // Recalculate tier based on current order stats
        MembershipTier newTier = calculateUserTier(subscription.getUser());
        subscription.setTier(newTier);
        
        Subscription updatedSubscription = subscriptionRepository.save(subscription);
        return convertToDTO(updatedSubscription);
    }
    
    /**
     * Downgrade subscription tier.
     * 
     * @param subscriptionId the subscription ID
     * @param tierLevel the new tier level
     * @return updated subscription DTO
     */
    @Transactional
    public SubscriptionDTO downgradeTier(Long subscriptionId, MembershipTier.TierLevel tierLevel) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + subscriptionId));
        
        if (subscription.getStatus() != Subscription.SubscriptionStatus.ACTIVE) {
            throw new RuntimeException("Cannot downgrade inactive subscription");
        }
        
        MembershipTier newTier = tierRepository.findByTierLevel(tierLevel)
                .orElseThrow(() -> new RuntimeException("Tier not found with level: " + tierLevel));
        
        subscription.setTier(newTier);
        
        Subscription updatedSubscription = subscriptionRepository.save(subscription);
        return convertToDTO(updatedSubscription);
    }
    
    /**
     * Cancel a subscription.
     * 
     * @param subscriptionId the subscription ID
     * @return cancelled subscription DTO
     */
    @Transactional
    public SubscriptionDTO cancelSubscription(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + subscriptionId));
        
        subscription.setStatus(Subscription.SubscriptionStatus.CANCELLED);
        subscription.setAutoRenew(false);
        
        Subscription cancelledSubscription = subscriptionRepository.save(subscription);
        return convertToDTO(cancelledSubscription);
    }
    
    /**
     * Renew a subscription.
     * 
     * @param subscriptionId the subscription ID
     * @return renewed subscription DTO
     */
    @Transactional
    public SubscriptionDTO renewSubscription(Long subscriptionId) {
        Subscription oldSubscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + subscriptionId));
        
        // Mark old subscription as expired
        oldSubscription.setStatus(Subscription.SubscriptionStatus.EXPIRED);
        subscriptionRepository.save(oldSubscription);
        
        // Create new subscription
        SubscriptionRequest request = new SubscriptionRequest();
        request.setUserId(oldSubscription.getUser().getId());
        request.setPlanId(oldSubscription.getPlan().getId());
        request.setAutoRenew(oldSubscription.getAutoRenew());
        
        return createSubscription(request);
    }
    
    /**
     * Check and update expired subscriptions.
     */
    @Transactional
    public void checkAndUpdateExpiredSubscriptions() {
        List<Subscription> activeSubscriptions = subscriptionRepository
                .findByStatus(Subscription.SubscriptionStatus.ACTIVE);
        
        LocalDateTime now = LocalDateTime.now();
        
        for (Subscription subscription : activeSubscriptions) {
            if (subscription.getEndDate().isBefore(now)) {
                if (subscription.getAutoRenew()) {
                    renewSubscription(subscription.getId());
                } else {
                    subscription.setStatus(Subscription.SubscriptionStatus.EXPIRED);
                    subscriptionRepository.save(subscription);
                }
            }
        }
    }
    
    /**
     * Calculate appropriate tier for a user based on order history.
     * 
     * @param user the user
     * @return calculated membership tier
     */
    private MembershipTier calculateUserTier(User user) {
        List<MembershipTier> eligibleTiers = tierRepository.findEligibleTiers(
                user.getTotalOrderCount(),
                user.getTotalOrderValue()
        );
        
        if (eligibleTiers.isEmpty()) {
            // Return default Silver tier
            return tierRepository.findByTierLevel(MembershipTier.TierLevel.SILVER)
                    .orElseThrow(() -> new RuntimeException("Default tier not found"));
        }
        
        return eligibleTiers.get(0);
    }
    
    /**
     * Convert Subscription entity to SubscriptionDTO.
     * 
     * @param subscription the subscription entity
     * @return subscription DTO
     */
    private SubscriptionDTO convertToDTO(Subscription subscription) {
        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setId(subscription.getId());
        dto.setUserId(subscription.getUser().getId());
        dto.setUserName(subscription.getUser().getName());
        dto.setUserEmail(subscription.getUser().getEmail());
        dto.setPlanId(subscription.getPlan().getId());
        dto.setPlanName(subscription.getPlan().getName());
        dto.setTierId(subscription.getTier().getId());
        dto.setTierName(subscription.getTier().getName());
        dto.setStatus(subscription.getStatus());
        dto.setStartDate(subscription.getStartDate());
        dto.setEndDate(subscription.getEndDate());
        dto.setAutoRenew(subscription.getAutoRenew());
        return dto;
    }
}
