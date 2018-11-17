package com.pillar.cucumber;

import com.pillar.account.Account;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRowMapper implements RowMapper
{
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Account(rs.getInt("ID"), rs.getDouble("CREDIT_LIMIT"), rs.getString("CREDIT_CARD_NUMBER"), rs.getBoolean("ACTIVE"));
    }

}