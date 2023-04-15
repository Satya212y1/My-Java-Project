package com.zenscale.zencrm_2.scaledb;

import com.google.gson.Gson;

import com.zenscale.zencrm_2.structures.struct_stngs;
import com.zenscale.zencrm_2.utils.JdbcTempUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StngDb {


    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcTempUtil jdbcTempUtil;




    public int checkExist(String stngId) {

        String query = "SELECT count(*) from stngs_m where stngId='" + stngId + "'";
        return jdbcTempUtil.getCount(query);
    }




    public List<struct_stngs> get_stngs(int bukrs, String stngid, String type, String parent_stng_id) {

        String query = "SELECT a.stngId,a.descr , a.type , a.parent , b.status FROM stngs_m as a left join " +
                " stngs_bukrs as b on a.stngId = b.stngId and b.bukrs = " + bukrs;
        if (stngid.length() > 0) {
            query += " and a.stngId = '" + stngid + "'";
        }
        if (type.length() > 0) {
            query += " and a.type = '" + type + "'";
        }
        if (parent_stng_id.length() > 0) {
            query += " and a.parent = '" + parent_stng_id + "'";
        }

        List<struct_stngs> d0 = jdbcTemplate.query(query, new BeanPropertyRowMapper(struct_stngs.class));
        return d0;
    }




    public boolean checkJobOrderDataExist(int bukrs) {

        String query = "SELECT jobno from joborder_h where bukrs = " + bukrs + " LIMIT 1 OFFSET 0";
        int count = jdbcTempUtil.getCount(query);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }




}
