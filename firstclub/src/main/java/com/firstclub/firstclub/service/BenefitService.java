package com.firstclub.firstclub.service;

import com.firstclub.firstclub.dto.BenefitDTO;
import com.firstclub.firstclub.model.Benefit;
import com.firstclub.firstclub.model.MembershipTier;
import com.firstclub.firstclub.repository.BenefitRepository;
import com.firstclub.firstclub.repository.MembershipTierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Benefit-related business logic.
 */
@Service
@RequiredArgsConstructor
public class BenefitService {
    
    private final BenefitRepository benefitRepository;
    private final MembershipTierRepository tierRepository;
    
    /**
     * Create a new benefit.
     * 
     * @param benefitDTO the benefit data
     * @return created benefit DTO
     */
    @Transactional
    public BenefitDTO createBenefit(BenefitDTO benefitDTO) {
        MembershipTier tier = tierRepository.findById(benefitDTO.getTierId())
                .orElseThrow(() -> new RuntimeException("Tier not found with id: " + benefitDTO.getTierId()));
        
        Benefit benefit = new Benefit();
        benefit.setTier(tier);
        benefit.setName(benefitDTO.getName());
        benefit.setDescription(benefitDTO.getDescription());
        benefit.setBenefitType(benefitDTO.getBenefitType());
        benefit.setValue(benefitDTO.getValue());
        benefit.setActive(benefitDTO.getActive() != null ? benefitDTO.getActive() : true);
        
        Benefit savedBenefit = benefitRepository.save(benefit);
        return convertToDTO(savedBenefit);
    }
    
    /**
     * Get benefit by ID.
     * 
     * @param id the benefit ID
     * @return benefit DTO
     */
    public BenefitDTO getBenefitById(Long id) {
        Benefit benefit = benefitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Benefit not found with id: " + id));
        return convertToDTO(benefit);
    }
    
    /**
     * Get all benefits.
     * 
     * @return list of benefit DTOs
     */
    public List<BenefitDTO> getAllBenefits() {
        return benefitRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all benefits for a specific tier.
     * 
     * @param tierId the tier ID
     * @return list of benefit DTOs
     */
    public List<BenefitDTO> getBenefitsByTier(Long tierId) {
        return benefitRepository.findByTierId(tierId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all active benefits for a tier.
     * 
     * @param tierId the tier ID
     * @return list of active benefit DTOs
     */
    public List<BenefitDTO> getActiveBenefitsByTier(Long tierId) {
        return benefitRepository.findByTierIdAndActive(tierId, true).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Update a benefit.
     * 
     * @param id the benefit ID
     * @param benefitDTO the updated benefit data
     * @return updated benefit DTO
     */
    @Transactional
    public BenefitDTO updateBenefit(Long id, BenefitDTO benefitDTO) {
        Benefit benefit = benefitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Benefit not found with id: " + id));
        
        if (benefitDTO.getTierId() != null && !benefitDTO.getTierId().equals(benefit.getTier().getId())) {
            MembershipTier tier = tierRepository.findById(benefitDTO.getTierId())
                    .orElseThrow(() -> new RuntimeException("Tier not found with id: " + benefitDTO.getTierId()));
            benefit.setTier(tier);
        }
        
        benefit.setName(benefitDTO.getName());
        benefit.setDescription(benefitDTO.getDescription());
        benefit.setBenefitType(benefitDTO.getBenefitType());
        benefit.setValue(benefitDTO.getValue());
        if (benefitDTO.getActive() != null) {
            benefit.setActive(benefitDTO.getActive());
        }
        
        Benefit updatedBenefit = benefitRepository.save(benefit);
        return convertToDTO(updatedBenefit);
    }
    
    /**
     * Delete a benefit.
     * 
     * @param id the benefit ID
     */
    @Transactional
    public void deleteBenefit(Long id) {
        if (!benefitRepository.existsById(id)) {
            throw new RuntimeException("Benefit not found with id: " + id);
        }
        benefitRepository.deleteById(id);
    }
    
    /**
     * Convert Benefit entity to BenefitDTO.
     * 
     * @param benefit the benefit entity
     * @return benefit DTO
     */
    private BenefitDTO convertToDTO(Benefit benefit) {
        BenefitDTO dto = new BenefitDTO();
        dto.setId(benefit.getId());
        dto.setTierId(benefit.getTier().getId());
        dto.setTierName(benefit.getTier().getName());
        dto.setName(benefit.getName());
        dto.setDescription(benefit.getDescription());
        dto.setBenefitType(benefit.getBenefitType());
        dto.setValue(benefit.getValue());
        dto.setActive(benefit.getActive());
        return dto;
    }
}
