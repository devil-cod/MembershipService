package com.firstclub.firstclub.service;

import com.firstclub.firstclub.dto.MembershipPlanDTO;
import com.firstclub.firstclub.model.MembershipPlan;
import com.firstclub.firstclub.repository.MembershipPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for MembershipPlan-related business logic.
 */
@Service
@RequiredArgsConstructor
public class MembershipPlanService {
    
    private final MembershipPlanRepository planRepository;
    
    /**
     * Create a new membership plan.
     * 
     * @param planDTO the plan data
     * @return created plan DTO
     */
    @Transactional
    public MembershipPlanDTO createPlan(MembershipPlanDTO planDTO) {
        MembershipPlan plan = new MembershipPlan();
        plan.setName(planDTO.getName());
        plan.setPlanType(planDTO.getPlanType());
        plan.setPrice(planDTO.getPrice());
        plan.setDurationInDays(planDTO.getDurationInDays());
        plan.setDescription(planDTO.getDescription());
        plan.setActive(planDTO.getActive() != null ? planDTO.getActive() : true);
        
        MembershipPlan savedPlan = planRepository.save(plan);
        return convertToDTO(savedPlan);
    }
    
    /**
     * Get plan by ID.
     * 
     * @param id the plan ID
     * @return plan DTO
     */
    public MembershipPlanDTO getPlanById(Long id) {
        MembershipPlan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + id));
        return convertToDTO(plan);
    }
    
    /**
     * Get all plans.
     * 
     * @return list of plan DTOs
     */
    public List<MembershipPlanDTO> getAllPlans() {
        return planRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all active plans.
     * 
     * @return list of active plan DTOs
     */
    public List<MembershipPlanDTO> getActivePlans() {
        return planRepository.findByActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Update a membership plan.
     * 
     * @param id the plan ID
     * @param planDTO the updated plan data
     * @return updated plan DTO
     */
    @Transactional
    public MembershipPlanDTO updatePlan(Long id, MembershipPlanDTO planDTO) {
        MembershipPlan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + id));
        
        plan.setName(planDTO.getName());
        plan.setPlanType(planDTO.getPlanType());
        plan.setPrice(planDTO.getPrice());
        plan.setDurationInDays(planDTO.getDurationInDays());
        plan.setDescription(planDTO.getDescription());
        if (planDTO.getActive() != null) {
            plan.setActive(planDTO.getActive());
        }
        
        MembershipPlan updatedPlan = planRepository.save(plan);
        return convertToDTO(updatedPlan);
    }
    
    /**
     * Delete a plan.
     * 
     * @param id the plan ID
     */
    @Transactional
    public void deletePlan(Long id) {
        if (!planRepository.existsById(id)) {
            throw new RuntimeException("Plan not found with id: " + id);
        }
        planRepository.deleteById(id);
    }
    
    /**
     * Convert MembershipPlan entity to MembershipPlanDTO.
     * 
     * @param plan the plan entity
     * @return plan DTO
     */
    private MembershipPlanDTO convertToDTO(MembershipPlan plan) {
        MembershipPlanDTO dto = new MembershipPlanDTO();
        dto.setId(plan.getId());
        dto.setName(plan.getName());
        dto.setPlanType(plan.getPlanType());
        dto.setPrice(plan.getPrice());
        dto.setDurationInDays(plan.getDurationInDays());
        dto.setDescription(plan.getDescription());
        dto.setActive(plan.getActive());
        return dto;
    }
}
