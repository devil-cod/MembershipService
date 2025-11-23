package com.firstclub.firstclub.repository;

import com.firstclub.firstclub.model.MembershipPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Long> {
    List<MembershipPlan> findByActiveTrue();
    Optional<MembershipPlan> findByPlanType(MembershipPlan.PlanType planType);
    Optional<MembershipPlan> findByName(String name);
}
