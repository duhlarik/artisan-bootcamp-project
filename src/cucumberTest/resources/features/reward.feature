Feature:  As a credit card processor I want to know how many points are owed an account on which was made transactions at a retailer

  Scenario: Create rewards programmes
    Given a retailer offering a reward programme
    Then the reward programme is created

  Scenario: Receive rewards for qualifying transactions
    Given an account with a charge balance of 10 at TARGET
      And the retailer TARGET offering 1% rewards
    When someone requests the TARGET rewards balance
    Then they see a balance of 0.1


  Scenario: Don’t receive rewards for non-qualifying transactions
    Given an account with a charge balance of 10 at TARGET
      And the retailer BESTBUY offering 1% rewards
    When someone requests the TARGET rewards balance
    Then they see a balance of 0
