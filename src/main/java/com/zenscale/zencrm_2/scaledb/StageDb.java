package com.zenscale.zencrm_2.scaledb;

import com.zenscale.zencrm_2.structures.struct_stages;
import com.zenscale.zencrm_2.utils.JdbcTempUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StageDb {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcTempUtil jdbcTempUtil;




    public int getTotalRecordsStagesCount(int bukrs, int stageid, String search) {

        String sql = "select count(*) from stages_m where bukrs = " + bukrs + " and delstats = ''";
        if (stageid > 0) {
            sql += " and stageId = " + stageid;
        } else if (search.length() > 0 && stageid == 0) {
            sql += " and ( " +
                    " LOWER(descr) LIKE '%" + search + "%' " +
                    " )";
        }
        //System.out.println("sql = " + sql);
        return jdbcTempUtil.getCount(sql);
    }




    public List<struct_stages> getTotalRecordsStages(int bukrs, int stageid, String search) {

        String sql = "select stageId, descr, status, colorCode from stages_m where bukrs = " + bukrs + " and delstats = ''";
        if (stageid > 0) {
            sql += " and stageId = " + stageid;
        } else if (search.length() > 0 && stageid == 0) {
            sql += " and ( " +
                    " LOWER(descr) LIKE '%" + search + "%' " +
                    " )";
        }
        sql += " ORDER BY creon DESC ";
        //System.out.println("sql = " + sql);
        List<struct_stages> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(struct_stages.class));
        return list;
    }




}
