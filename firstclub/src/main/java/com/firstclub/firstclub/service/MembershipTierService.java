package com.firstclub.firstclub.service;

import com.firstclub.firstclub.dto.MembershipTierDTO;
import com.firstclub.firstclub.model.MembershipTier;
import com.firstclub.firstclub.model.User;
import com.firstclub.firstclub.repository.MembershipTierRepository;
import com.firstclub.firstclub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for MembershipTier-related business logic.
 */
@Service
@RequiredArgsConstructor
public class MembershipTierService {
    
    private final MembershipTierRepository tierRepository;
    private final UserRepository userRepository;
    
    /**
     * Create a new membership tier.
     * 
     * @param tierDTO the tier data
     * @return created tier DTO
     */
    @Transactional
    public MembershipTierDTO createTier(MembershipTierDTO tierDTO) {
        MembershipTier tier = new MembershipTier();
        tier.setName(tierDTO.getName());
        tier.setTierLevel(tierDTO.getTierLevel());
        tier.setMinOrderCount(tierDTO.getMinOrderCount());
        tier.setMinOrderValue(tierDTO.getMinOrderValue());
        tier.setDiscountPercentage(tierDTO.getDiscountPercentage());
        tier.setFreeDelivery(tierDTO.getFreeDelivery() != null ? tierDTO.getFreeDelivery() : false);
        tier.setPrioritySupport(tierDTO.getPrioritySupport() != null ? tierDTO.getPrioritySupport() : false);
        tier.setExclusiveDeals(tierDTO.getExclusiveDeals() != null ? tierDTO.getExclusiveDeals() : false);
        tier.setDescription(tierDTO.getDescription());
        tier.setActive(tierDTO.getActive() != null ? tierDTO.getActive() : true);
        
        MembershipTier savedTier = tierRepository.save(tier);
        return convertToDTO(savedTier);
    }
    
    /**
     * Get tier by ID.
     * 
     * @param id the tier ID
     * @return tier DTO
     */
    public MembershipTierDTO getTierById(Long id) {
        MembershipTier tier = tierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tier not found with id: " + id));
        return convertToDTO(tier);
    }
    
    /**
     * Get all tiers.
     * 
     * @return list of tier DTOs
     */
    public List<MembershipTierDTO> getAllTiers() {
        return tierRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all active tiers.
     * 
     * @return list of active tier DTOs
     */
    public List<MembershipTierDTO> getActiveTiers() {
        return tierRepository.findByActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Calculate eligible tier for a user based on their order history.
     * 
     * @param userId the user ID
     * @return eligible tier DTO
     */
    public MembershipTierDTO calculateEligibleTier(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        List<MembershipTier> eligibleTiers = tierRepository.findEligibleTiers(
                user.getTotalOrderCount(), 
                user.getTotalOrderValue()
        );
        
        if (eligibleTiers.isEmpty()) {
            // Return default Silver tier if no tier matches
            return tierRepository.findByTierLevel(MembershipTier.TierLevel.SILVER)
                    .map(this::convertToDTO)
                    .orElseThrow(() -> new RuntimeException("Default tier not found"));
        }
        
        return convertToDTO(eligibleTiers.get(0));
    }
    
    /**
     * Update a membership tier.
     * 
     * @param id the tier ID
     * @param tierDTO the updated tier data
     * @return updated tier DTO
     */
    @Transactional
    public MembershipTierDTO updateTier(Long id, MembershipTierDTO tierDTO) {
        MembershipTier tier = tierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tier not found with id: " + id));
        
        tier.setName(tierDTO.getName());
        tier.setTierLevel(tierDTO.getTierLevel());
        tier.setMinOrderCount(tierDTO.getMinOrderCount());
        tier.setMinOrderValue(tierDTO.getMinOrderValue());
        tier.setDiscountPercentage(tierDTO.getDiscountPercentage());
        tier.setFreeDelivery(tierDTO.getFreeDelivery());
        tier.setPrioritySupport(tierDTO.getPrioritySupport());
        tier.setExclusiveDeals(tierDTO.getExclusiveDeals());
        tier.setDescription(tierDTO.getDescription());
        if (tierDTO.getActive() != null) {
            tier.setActive(tierDTO.getActive());
        }
        
        MembershipTier updatedTier = tierRepository.save(tier);
        return convertToDTO(updatedTier);
    }
    
    /**
     * Delete a tier.
     * 
     * @param id the tier ID
     */
    @Transactional
    public void deleteTier(Long id) {
        if (!tierRepository.existsById(id)) {
            throw new RuntimeException("Tier not found with id: " + id);
        }
        tierRepository.deleteById(id);
    }
    
    /**
     * Convert MembershipTier entity to MembershipTierDTO.
     * 
     * @param tier the tier entity
     * @return tier DTO
     */
    private MembershipTierDTO convertToDTO(MembershipTier tier) {
        MembershipTierDTO dto = new MembershipTierDTO();
        dto.setId(tier.getId());
        dto.setName(tier.getName());
        dto.setTierLevel(tier.getTierLevel());
        dto.setMinOrderCount(tier.getMinOrderCount());
        dto.setMinOrderValue(tier.getMinOrderValue());
        dto.setDiscountPercentage(tier.getDiscountPercentage());
        dto.setFreeDelivery(tier.getFreeDelivery());
        dto.setPrioritySupport(tier.getPrioritySupport());
        dto.setExclusiveDeals(tier.getExclusiveDeals());
        dto.setDescription(tier.getDescription());
        dto.setActive(tier.getActive());
        return dto;
    }
}
