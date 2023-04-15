package com.zenscale.zencrm_2.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class JdbcTempUtil {


    @Autowired
    JdbcTemplate jdbcTemplate;




    public int getCount(String query) {

        Integer c = 0;
        try {
            c = jdbcTemplate.queryForObject(query, Integer.class);
        } catch (DataAccessException e) {

        }
        if (c == null) {
            return 0;
        } else {
            return c;
        }
    }




}
