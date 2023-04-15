package com.zenscale.zencrm_2.scaledb;

import com.zenscale.zencrm_2.utils.JdbcTempUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class CommonDb {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcTempUtil jdbcTempUtil;




    public int isUidExist(int bukrs, String uid) {

        String sql = "select count(*) from reg_users where bukrs = " + bukrs + " and uid = '" + uid + "'";
        return jdbcTempUtil.getCount(sql);
    }




}
