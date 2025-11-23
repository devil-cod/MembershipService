package com.firstclub.firstclub.controller;

import com.firstclub.firstclub.dto.ApiResponse;
import com.firstclub.firstclub.dto.BenefitDTO;
import com.firstclub.firstclub.service.BenefitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Benefit management endpoints.
 */
@RestController
@RequestMapping("/api/benefits")
@RequiredArgsConstructor
public class BenefitController {
    
    private final BenefitService benefitService;
    
    /**
     * Create a new benefit.
     * 
     * @param benefitDTO the benefit data
     * @return created benefit
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BenefitDTO>> createBenefit(@Valid @RequestBody BenefitDTO benefitDTO) {
        try {
            BenefitDTO createdBenefit = benefitService.createBenefit(benefitDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Benefit created successfully", createdBenefit));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Get benefit by ID.
     * 
     * @param id the benefit ID
     * @return benefit details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BenefitDTO>> getBenefitById(@PathVariable Long id) {
        try {
            BenefitDTO benefit = benefitService.getBenefitById(id);
            return ResponseEntity.ok(ApiResponse.success(benefit));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Get all benefits.
     * 
     * @return list of all benefits
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BenefitDTO>>> getAllBenefits() {
        List<BenefitDTO> benefits = benefitService.getAllBenefits();
        return ResponseEntity.ok(ApiResponse.success(benefits));
    }
    
    /**
     * Get all benefits for a specific tier.
     * 
     * @param tierId the tier ID
     * @return list of benefits for the tier
     */
    @GetMapping("/tier/{tierId}")
    public ResponseEntity<ApiResponse<List<BenefitDTO>>> getBenefitsByTier(@PathVariable Long tierId) {
        try {
            List<BenefitDTO> benefits = benefitService.getBenefitsByTier(tierId);
            return ResponseEntity.ok(ApiResponse.success(benefits));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Get all active benefits for a tier.
     * 
     * @param tierId the tier ID
     * @return list of active benefits
     */
    @GetMapping("/tier/{tierId}/active")
    public ResponseEntity<ApiResponse<List<BenefitDTO>>> getActiveBenefitsByTier(@PathVariable Long tierId) {
        try {
            List<BenefitDTO> benefits = benefitService.getActiveBenefitsByTier(tierId);
            return ResponseEntity.ok(ApiResponse.success(benefits));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Update a benefit.
     * 
     * @param id the benefit ID
     * @param benefitDTO the updated benefit data
     * @return updated benefit
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BenefitDTO>> updateBenefit(
            @PathVariable Long id,
            @Valid @RequestBody BenefitDTO benefitDTO) {
        try {
            BenefitDTO updatedBenefit = benefitService.updateBenefit(id, benefitDTO);
            return ResponseEntity.ok(ApiResponse.success("Benefit updated successfully", updatedBenefit));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Delete a benefit.
     * 
     * @param id the benefit ID
     * @return success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBenefit(@PathVariable Long id) {
        try {
            benefitService.deleteBenefit(id);
            return ResponseEntity.ok(ApiResponse.success("Benefit deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
