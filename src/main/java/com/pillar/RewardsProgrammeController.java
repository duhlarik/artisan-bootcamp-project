package com.pillar;

import com.pillar.rewardsProgramme.RewardsProgramme;
import com.pillar.rewardsProgramme.RewardsProgrammeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rewards")
public class RewardsProgrammeController {

    private RewardsProgrammeRepository rewardsProgrammeRepository;

    public RewardsProgrammeController(RewardsProgrammeRepository rewardsProgrammeRepository) {
        this.rewardsProgrammeRepository = rewardsProgrammeRepository;
    }

    @RequestMapping("/{retailer}")
    public ResponseEntity<RewardsProgramme> createRewardsProgramme(@PathVariable String retailer, @RequestBody double percentage) {
        RewardsProgramme rewardsProgramme = new RewardsProgramme(retailer, percentage);
        rewardsProgrammeRepository.save(rewardsProgramme);
        return new ResponseEntity<>(rewardsProgramme, HttpStatus.CREATED);
    }
}
