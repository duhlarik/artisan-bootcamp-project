package com.pillar.integration;

import com.pillar.rewardsProgramme.RewardsProgramme;
import com.pillar.rewardsProgramme.RewardsProgrammeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Rollback
@RunWith(SpringRunner.class)
public class TestRewardsProgrammeRepository {
    @Autowired
    private RewardsProgrammeRepository rewardsProgrammeRepository;

    @Test
    public void findOneByRetailerReturnsRewardsProgrammeWith1PercentWhenRewardsProgrammeOfSameNameAnd1PercentIsSaved() {
        String retailer = "TARGET";
        double percentage = 1.0;
        rewardsProgrammeRepository.save(new RewardsProgramme(retailer, percentage));

        Optional<RewardsProgramme> fromRepository = rewardsProgrammeRepository.findOneByRetailer(retailer);

        assertEquals(percentage, fromRepository.get().getPercentage(), 0.001);
    }
}
