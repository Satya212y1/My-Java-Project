package com.zenscale.zencrm_2.scaledb;

import com.zenscale.zencrm_2.structures.struct_source;
import com.zenscale.zencrm_2.structures.struct_stages;
import com.zenscale.zencrm_2.utils.JdbcTempUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SourceDb {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcTempUtil jdbcTempUtil;




    public int getTotalRecordsSourceCount(int bukrs, int sourceid, String search) {

        String sql = "select count(*) from source_m where bukrs = " + bukrs + " and delstats = '' ";
        if (sourceid > 0) {
            sql += " and sourceId = " + sourceid;
        } else if (search.length() > 0 && sourceid == 0) {
            sql += " and ( " +
                    " LOWER(descr) LIKE '%" + search + "%' " +
                    " )";
        }
        //System.out.println("sql = " + sql);
        return jdbcTempUtil.getCount(sql);
    }




    public List<struct_source> getTotalRecordsSources(int bukrs, int sourceid, String search) {

        String sql = "select sourceId, descr from source_m where bukrs = " + bukrs + " and delstats = '' ";
        if (sourceid > 0) {
            sql += " and sourceId = " + sourceid;
        } else if (search.length() > 0 && sourceid == 0) {
            sql += " and ( " +
                    " LOWER(descr) LIKE '%" + search + "%' " +
                    " )";
        }
        sql += " ORDER BY sourceId DESC";
        //System.out.println("sql = " + sql);
        List<struct_source> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(struct_source.class));
        return list;
    }




}
