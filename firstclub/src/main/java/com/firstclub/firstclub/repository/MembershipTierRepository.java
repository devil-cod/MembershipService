package com.firstclub.firstclub.repository;

import com.firstclub.firstclub.model.MembershipTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipTierRepository extends JpaRepository<MembershipTier, Long> {
    List<MembershipTier> findByActiveTrue();
    Optional<MembershipTier> findByTierLevel(MembershipTier.TierLevel tierLevel);
    
    @Query("SELECT t FROM MembershipTier t WHERE t.active = true " +
           "AND (t.minOrderCount IS NULL OR t.minOrderCount <= :orderCount) " +
           "AND (t.minOrderValue IS NULL OR t.minOrderValue <= :orderValue) " +
           "ORDER BY t.minOrderCount DESC, t.minOrderValue DESC")
    List<MembershipTier> findEligibleTiers(@Param("orderCount") Integer orderCount, 
                                           @Param("orderValue") Long orderValue);
}
