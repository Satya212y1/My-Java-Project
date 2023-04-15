package com.zenscale.zencrm_2.scaledb;

import com.zenscale.zencrm_2.utils.JdbcTempUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthDb {

    @Autowired
    JdbcTempUtil jdbcTempUtil;




    public int get_bukrs_apiKey(String apiKey) {

        String query = "SELECT bukrs FROM bukrs_subscribed where api_key = '" + apiKey + "'";
        return jdbcTempUtil.getCount(query);
    }




    public int get_bukrs_token(String token) {

        String query = "SELECT bukrs FROM access_token where token = '" + token + "'";
        return jdbcTempUtil.getCount(query);
    }




}
