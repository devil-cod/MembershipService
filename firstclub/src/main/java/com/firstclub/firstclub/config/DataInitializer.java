package com.firstclub.firstclub.config;

import com.firstclub.firstclub.model.MembershipPlan;
import com.firstclub.firstclub.model.MembershipTier;
import com.firstclub.firstclub.repository.MembershipPlanRepository;
import com.firstclub.firstclub.repository.MembershipTierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final MembershipPlanRepository planRepository;
    private final MembershipTierRepository tierRepository;
    
    @Override
    public void run(String... args) {
        initializeMembershipPlans();
        initializeMembershipTiers();
    }
    
    private void initializeMembershipPlans() {
        if (planRepository.count() == 0) {
            MembershipPlan monthlyPlan = new MembershipPlan();
            monthlyPlan.setName("Monthly Membership");
            monthlyPlan.setPlanType(MembershipPlan.PlanType.MONTHLY);
            monthlyPlan.setPrice(999L);
            monthlyPlan.setDurationInDays(30);
            monthlyPlan.setDescription("Monthly subscription with all basic benefits");
            monthlyPlan.setActive(true);
            planRepository.save(monthlyPlan);
            
            MembershipPlan quarterlyPlan = new MembershipPlan();
            quarterlyPlan.setName("Quarterly Membership");
            quarterlyPlan.setPlanType(MembershipPlan.PlanType.QUARTERLY);
            quarterlyPlan.setPrice(2499L);
            quarterlyPlan.setDurationInDays(90);
            quarterlyPlan.setDescription("Quarterly subscription with 15% savings");
            quarterlyPlan.setActive(true);
            planRepository.save(quarterlyPlan);
            
            MembershipPlan yearlyPlan = new MembershipPlan();
            yearlyPlan.setName("Yearly Membership");
            yearlyPlan.setPlanType(MembershipPlan.PlanType.YEARLY);
            yearlyPlan.setPrice(8999L);
            yearlyPlan.setDurationInDays(365);
            yearlyPlan.setDescription("Yearly subscription with 25% savings");
            yearlyPlan.setActive(true);
            planRepository.save(yearlyPlan);
            
            System.out.println("Loaded 3 membership plans");
        }
    }
    
    private void initializeMembershipTiers() {
        if (tierRepository.count() == 0) {
            MembershipTier silverTier = new MembershipTier();
            silverTier.setName("Silver");
            silverTier.setTierLevel(MembershipTier.TierLevel.SILVER);
            silverTier.setMinOrderCount(0);
            silverTier.setMinOrderValue(0L);
            silverTier.setDiscountPercentage(5L);
            silverTier.setFreeDelivery(true);
            silverTier.setPrioritySupport(false);
            silverTier.setExclusiveDeals(false);
            silverTier.setDescription("Entry level tier with 5% discount and free delivery");
            silverTier.setActive(true);
            tierRepository.save(silverTier);
            
            MembershipTier goldTier = new MembershipTier();
            goldTier.setName("Gold");
            goldTier.setTierLevel(MembershipTier.TierLevel.GOLD);
            goldTier.setMinOrderCount(10);
            goldTier.setMinOrderValue(50000L);
            goldTier.setDiscountPercentage(10L);
            goldTier.setFreeDelivery(true);
            goldTier.setPrioritySupport(false);
            goldTier.setExclusiveDeals(true);
            goldTier.setDescription("Mid-level tier with 10% discount, free delivery, and exclusive deals");
            goldTier.setActive(true);
            tierRepository.save(goldTier);
            
            MembershipTier platinumTier = new MembershipTier();
            platinumTier.setName("Platinum");
            platinumTier.setTierLevel(MembershipTier.TierLevel.PLATINUM);
            platinumTier.setMinOrderCount(25);
            platinumTier.setMinOrderValue(150000L);
            platinumTier.setDiscountPercentage(15L);
            platinumTier.setFreeDelivery(true);
            platinumTier.setPrioritySupport(true);
            platinumTier.setExclusiveDeals(true);
            platinumTier.setDescription("Premium tier with 15% discount, free delivery, priority support, and exclusive deals");
            platinumTier.setActive(true);
            tierRepository.save(platinumTier);
            
            System.out.println("Loaded 3 membership tiers");
        }
    }
}
