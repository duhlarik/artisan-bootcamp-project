package com.pillar.integration;

import com.pillar.RewardsController;
import com.pillar.RewardsProgramme;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Rollback
@RunWith(SpringRunner.class)
public class TestRewardsController {

    @Autowired
    RewardsController rewardsController;

    @Test
    public void createRewardProgrammeReturns201CreatedGivenARetailerAndAPercentage() {
        ResponseEntity<RewardsProgramme> response = rewardsController.createRewardsProgramme("TARGET", 1.0);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

}
