package com.firstclub.firstclub.controller;

import com.firstclub.firstclub.dto.ApiResponse;
import com.firstclub.firstclub.dto.SubscriptionDTO;
import com.firstclub.firstclub.dto.SubscriptionRequest;
import com.firstclub.firstclub.model.MembershipTier;
import com.firstclub.firstclub.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Subscription management endpoints.
 */
@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    
    private final SubscriptionService subscriptionService;
    
    /**
     * Create a new subscription.
     * 
     * @param request the subscription request
     * @return created subscription
     */
    @PostMapping
    public ResponseEntity<ApiResponse<SubscriptionDTO>> createSubscription(
            @Valid @RequestBody SubscriptionRequest request) {
        try {
            SubscriptionDTO subscription = subscriptionService.createSubscription(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Subscription created successfully", subscription));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Get subscription by ID.
     * 
     * @param id the subscription ID
     * @return subscription details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubscriptionDTO>> getSubscriptionById(@PathVariable Long id) {
        try {
            SubscriptionDTO subscription = subscriptionService.getSubscriptionById(id);
            return ResponseEntity.ok(ApiResponse.success(subscription));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Get all subscriptions.
     * 
     * @return list of all subscriptions
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<SubscriptionDTO>>> getAllSubscriptions() {
        List<SubscriptionDTO> subscriptions = subscriptionService.getAllSubscriptions();
        return ResponseEntity.ok(ApiResponse.success(subscriptions));
    }
    
    /**
     * Get all subscriptions for a user.
     * 
     * @param userId the user ID
     * @return list of user subscriptions
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<SubscriptionDTO>>> getUserSubscriptions(@PathVariable Long userId) {
        try {
            List<SubscriptionDTO> subscriptions = subscriptionService.getUserSubscriptions(userId);
            return ResponseEntity.ok(ApiResponse.success(subscriptions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Get active subscription for a user.
     * 
     * @param userId the user ID
     * @return active subscription
     */
    @GetMapping("/user/{userId}/active")
    public ResponseEntity<ApiResponse<SubscriptionDTO>> getActiveSubscription(@PathVariable Long userId) {
        try {
            SubscriptionDTO subscription = subscriptionService.getActiveSubscription(userId);
            return ResponseEntity.ok(ApiResponse.success(subscription));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Upgrade subscription tier.
     * 
     * @param id the subscription ID
     * @return upgraded subscription
     */
    @PatchMapping("/{id}/upgrade")
    public ResponseEntity<ApiResponse<SubscriptionDTO>> upgradeTier(@PathVariable Long id) {
        try {
            SubscriptionDTO subscription = subscriptionService.upgradeTier(id);
            return ResponseEntity.ok(ApiResponse.success("Tier upgraded successfully", subscription));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Downgrade subscription tier.
     * 
     * @param id the subscription ID
     * @param tierLevel the new tier level
     * @return downgraded subscription
     */
    @PatchMapping("/{id}/downgrade")
    public ResponseEntity<ApiResponse<SubscriptionDTO>> downgradeTier(
            @PathVariable Long id,
            @RequestParam MembershipTier.TierLevel tierLevel) {
        try {
            SubscriptionDTO subscription = subscriptionService.downgradeTier(id, tierLevel);
            return ResponseEntity.ok(ApiResponse.success("Tier downgraded successfully", subscription));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Cancel a subscription.
     * 
     * @param id the subscription ID
     * @return cancelled subscription
     */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<SubscriptionDTO>> cancelSubscription(@PathVariable Long id) {
        try {
            SubscriptionDTO subscription = subscriptionService.cancelSubscription(id);
            return ResponseEntity.ok(ApiResponse.success("Subscription cancelled successfully", subscription));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Renew a subscription.
     * 
     * @param id the subscription ID
     * @return renewed subscription
     */
    @PostMapping("/{id}/renew")
    public ResponseEntity<ApiResponse<SubscriptionDTO>> renewSubscription(@PathVariable Long id) {
        try {
            SubscriptionDTO subscription = subscriptionService.renewSubscription(id);
            return ResponseEntity.ok(ApiResponse.success("Subscription renewed successfully", subscription));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
