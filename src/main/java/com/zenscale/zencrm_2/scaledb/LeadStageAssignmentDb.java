package com.zenscale.zencrm_2.scaledb;

import com.zenscale.zencrm_2.structures.struct_lead_assignment;
import com.zenscale.zencrm_2.utils.CommonFunctions;
import com.zenscale.zencrm_2.utils.CommonStrings;
import com.zenscale.zencrm_2.utils.JdbcTempUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeadStageAssignmentDb {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcTempUtil jdbcTempUtil;

    CommonFunctions cf = new CommonFunctions();




    public int getCount(int bukrs, int stageId, int leadId) {

        String sql = "select count(*) from lead_stage_assignment where bukrs = " + bukrs;
        if (leadId > 0) {
            sql += " and leadId = " + leadId;
        }
        if (stageId > 0) {
            sql += " and stageId = " + stageId;
        }
        //System.out.println("sql = " + sql);
        return jdbcTempUtil.getCount(sql);
    }




    public List<struct_lead_assignment> getRecords(int bukrs, int stageId, int leadId) {

        String sql = "select a.leadId, a.stats, a.stageId, b.name as leadDesc, c.descr as stageDesc, a.creon from lead_stage_assignment as a ";
        sql += " left join leads as b on a.bukrs = b.bukrs and a.leadId = b.leadId ";
        sql += " left join stages_m as c on a.bukrs = c.bukrs and a.stageId = c.stageId ";
        sql += " where b.delstats = '' and b.stats = '' and c.delstats = '' and a.bukrs = " + bukrs;
        if (leadId > 0) {
            sql += " and a.leadId = " + leadId;
        }
        if (stageId > 0) {
            sql += " and a.stageId = " + stageId;
        }
        sql += " ORDER BY a.creon DESC";
        //System.out.println("sql = " + sql);
        List<struct_lead_assignment> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(struct_lead_assignment.class));

        if (list.size() > 0) {
            list.get(0).setDuration("Ongoing");
        }
        for (int i = 0; i < list.size() - 1; i++) {
            int currentIndex = i, nextIndex = i + 1;
            String duration = cf.getTimeDifference(list.get(nextIndex).getCreon(), list.get(currentIndex).getCreon(), CommonStrings.dateformat_hyphen_yyyyMMdd_HHmmss);
            list.get(nextIndex).setDuration(duration);
        }

        return list;
    }




}
