package com.pillar.integration;

import com.pillar.rewardsProgramme.RewardsProgramme;
import com.pillar.rewardsProgramme.RewardsProgrammeRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Rollback
@RunWith(SpringRunner.class)
public class TestRewardsProgrammeRepository {
    private static final String RETAILER = "RETAILER";
    @Autowired
    private RewardsProgrammeRepository rewardsProgrammeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void findOneByRetailerReturnsRewardsProgrammeWith1PercentWhenRewardsProgrammeOfSameNameAnd1PercentIsSaved() {
        double percentage = 1.0;
        rewardsProgrammeRepository.save(new RewardsProgramme(RETAILER, percentage));

        Optional<RewardsProgramme> fromRepository = rewardsProgrammeRepository.findOneByRetailer(RETAILER);

        assertEquals(percentage, fromRepository.get().getPercentage(), 0.001);
    }

    @After
    public void tearDown() throws Exception {
        jdbcTemplate.execute("DELETE FROM rewards_programme");
    }
}
