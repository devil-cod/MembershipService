package com.firstclub.firstclub.repository;

import com.firstclub.firstclub.model.Subscription;
import com.firstclub.firstclub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUser(User user);
    List<Subscription> findByUserId(Long userId);
    Optional<Subscription> findByUserIdAndStatus(Long userId, Subscription.SubscriptionStatus status);
    List<Subscription> findByStatus(Subscription.SubscriptionStatus status);
    
    @Query("SELECT s FROM Subscription s WHERE s.status = :status AND s.endDate <= :endDate")
    List<Subscription> findExpiringSoon(@Param("status") Subscription.SubscriptionStatus status,
                                       @Param("endDate") LocalDateTime endDate);
}
