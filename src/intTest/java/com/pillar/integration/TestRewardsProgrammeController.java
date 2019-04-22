package com.pillar.integration;

import com.pillar.RewardsProgrammeController;
import com.pillar.rewardsProgramme.RewardsProgramme;
import com.pillar.rewardsProgramme.RewardsProgrammeRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Rollback
@RunWith(SpringRunner.class)
public class TestRewardsProgrammeController {
    private static final String RETAILER = "TARGET";
    private static final double PERCENTAGE = 1.0;

    @Autowired
    private RewardsProgrammeController rewardsProgrammeController;

    @Autowired
    private RewardsProgrammeRepository rewardsProgrammeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void createRewardProgrammeReturns201CreatedGivenARetailerAndAPercentage() {
        ResponseEntity<RewardsProgramme> response = rewardsProgrammeController.createRewardsProgramme("TARGET", 1.0);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void createRewardsProgrammeReturnsCreatedRewardsProgrammeGivenRetailerAndAPercentage() {
        rewardsProgrammeController.createRewardsProgramme(RETAILER, PERCENTAGE);

        Optional<RewardsProgramme> actual = rewardsProgrammeRepository.findOneByRetailer(RETAILER);
        assertEquals(PERCENTAGE, actual.get().getPercentage(), 0.001);
        assertEquals(RETAILER, actual.get().getRetailer());
    }

    @After
    public void tearDown() throws SQLException {
        jdbcTemplate.execute("DELETE FROM rewards_programme");
    }

}