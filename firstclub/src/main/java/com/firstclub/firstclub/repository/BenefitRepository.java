package com.firstclub.firstclub.repository;

import com.firstclub.firstclub.model.Benefit;
import com.firstclub.firstclub.model.MembershipTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenefitRepository extends JpaRepository<Benefit, Long> {
    List<Benefit> findByTier(MembershipTier tier);
    List<Benefit> findByTierId(Long tierId);
    List<Benefit> findByTierIdAndActive(Long tierId, Boolean active);
}
