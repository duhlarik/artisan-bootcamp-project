package com.pillar.rewardsProgramme;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RewardsProgrammeRepository extends JpaRepository<RewardsProgramme, Integer> {
    Optional<RewardsProgramme> findOneByRetailer(String retailer);
}
