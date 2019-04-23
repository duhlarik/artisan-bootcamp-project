package com.pillar.cucumber;

import com.mysql.cj.jdbc.MysqlDataSource;
import cucumber.api.java.After;
import org.springframework.jdbc.core.JdbcTemplate;

public class Hooks {
    @After
    public void tearDownAllRecords() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://localhost:3316/cc_processing?useSSL=false");
        dataSource.setUser("root");
        dataSource.setPassword("password");

        final JdbcTemplate template = new JdbcTemplate(dataSource);
        template.execute("DELETE FROM transaction_record");
        template.execute("DELETE FROM account");
        template.execute("DELETE FROM customer");
        template.execute("DELETE FROM cardholder");
        template.execute("DELETE FROM rewards_programme");
    }
}
