package com.mobileApplication.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobileApplication.models.SlotRecommendation;

@Repository
public interface SlotRecommendationRepository extends JpaRepository<SlotRecommendation, Long> {
	
	List<SlotRecommendation> findByUserInfoIdAndBatchNumber(Long userId, int batchNumber);
    int countDistinctBatchNumberByUserInfoId(Long userId);
    
    List<SlotRecommendation> findByUserInfoIdAndStatus(Long userId, String status);

    // Needed to look up the slot reco tied to a specific appointment
    java.util.Optional<SlotRecommendation> findById(Long id);

}
