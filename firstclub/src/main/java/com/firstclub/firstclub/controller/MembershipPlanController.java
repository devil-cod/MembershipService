package com.firstclub.firstclub.controller;

import com.firstclub.firstclub.dto.ApiResponse;
import com.firstclub.firstclub.dto.MembershipPlanDTO;
import com.firstclub.firstclub.service.MembershipPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Membership Plan management endpoints.
 */
@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class MembershipPlanController {
    
    private final MembershipPlanService planService;
    
    /**
     * Create a new membership plan.
     * 
     * @param planDTO the plan data
     * @return created plan
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MembershipPlanDTO>> createPlan(@Valid @RequestBody MembershipPlanDTO planDTO) {
        try {
            MembershipPlanDTO createdPlan = planService.createPlan(planDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Plan created successfully", createdPlan));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Get plan by ID.
     * 
     * @param id the plan ID
     * @return plan details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MembershipPlanDTO>> getPlanById(@PathVariable Long id) {
        try {
            MembershipPlanDTO plan = planService.getPlanById(id);
            return ResponseEntity.ok(ApiResponse.success(plan));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Get all plans.
     * 
     * @return list of all plans
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<MembershipPlanDTO>>> getAllPlans() {
        List<MembershipPlanDTO> plans = planService.getAllPlans();
        return ResponseEntity.ok(ApiResponse.success(plans));
    }
    
    /**
     * Get all active plans.
     * 
     * @return list of active plans
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<MembershipPlanDTO>>> getActivePlans() {
        List<MembershipPlanDTO> plans = planService.getActivePlans();
        return ResponseEntity.ok(ApiResponse.success(plans));
    }
    
    /**
     * Update a membership plan.
     * 
     * @param id the plan ID
     * @param planDTO the updated plan data
     * @return updated plan
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MembershipPlanDTO>> updatePlan(
            @PathVariable Long id,
            @Valid @RequestBody MembershipPlanDTO planDTO) {
        try {
            MembershipPlanDTO updatedPlan = planService.updatePlan(id, planDTO);
            return ResponseEntity.ok(ApiResponse.success("Plan updated successfully", updatedPlan));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Delete a plan.
     * 
     * @param id the plan ID
     * @return success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletePlan(@PathVariable Long id) {
        try {
            planService.deletePlan(id);
            return ResponseEntity.ok(ApiResponse.success("Plan deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
