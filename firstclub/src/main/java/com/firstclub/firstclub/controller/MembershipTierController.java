package com.firstclub.firstclub.controller;

import com.firstclub.firstclub.dto.ApiResponse;
import com.firstclub.firstclub.dto.MembershipTierDTO;
import com.firstclub.firstclub.service.MembershipTierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Membership Tier management endpoints.
 */
@RestController
@RequestMapping("/api/tiers")
@RequiredArgsConstructor
public class MembershipTierController {
    
    private final MembershipTierService tierService;
    
    /**
     * Create a new membership tier.
     * 
     * @param tierDTO the tier data
     * @return created tier
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MembershipTierDTO>> createTier(@Valid @RequestBody MembershipTierDTO tierDTO) {
        try {
            MembershipTierDTO createdTier = tierService.createTier(tierDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Tier created successfully", createdTier));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Get tier by ID.
     * 
     * @param id the tier ID
     * @return tier details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MembershipTierDTO>> getTierById(@PathVariable Long id) {
        try {
            MembershipTierDTO tier = tierService.getTierById(id);
            return ResponseEntity.ok(ApiResponse.success(tier));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Get all tiers.
     * 
     * @return list of all tiers
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<MembershipTierDTO>>> getAllTiers() {
        List<MembershipTierDTO> tiers = tierService.getAllTiers();
        return ResponseEntity.ok(ApiResponse.success(tiers));
    }
    
    /**
     * Get all active tiers.
     * 
     * @return list of active tiers
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<MembershipTierDTO>>> getActiveTiers() {
        List<MembershipTierDTO> tiers = tierService.getActiveTiers();
        return ResponseEntity.ok(ApiResponse.success(tiers));
    }
    
    /**
     * Calculate eligible tier for a user.
     * 
     * @param userId the user ID
     * @return eligible tier
     */
    @GetMapping("/calculate/{userId}")
    public ResponseEntity<ApiResponse<MembershipTierDTO>> calculateEligibleTier(@PathVariable Long userId) {
        try {
            MembershipTierDTO tier = tierService.calculateEligibleTier(userId);
            return ResponseEntity.ok(ApiResponse.success("Eligible tier calculated", tier));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Update a membership tier.
     * 
     * @param id the tier ID
     * @param tierDTO the updated tier data
     * @return updated tier
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MembershipTierDTO>> updateTier(
            @PathVariable Long id,
            @Valid @RequestBody MembershipTierDTO tierDTO) {
        try {
            MembershipTierDTO updatedTier = tierService.updateTier(id, tierDTO);
            return ResponseEntity.ok(ApiResponse.success("Tier updated successfully", updatedTier));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Delete a tier.
     * 
     * @param id the tier ID
     * @return success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTier(@PathVariable Long id) {
        try {
            tierService.deleteTier(id);
            return ResponseEntity.ok(ApiResponse.success("Tier deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
