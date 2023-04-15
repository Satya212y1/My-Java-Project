package com.zenscale.zencrm_2.scaledb;

import com.zenscale.zencrm_2.structures.lead_cmts;
import com.zenscale.zencrm_2.structures.struct_list_lead;
import com.zenscale.zencrm_2.utils.JdbcTempUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportDb {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcTempUtil jdbcTempUtil;




    public int getTotalLeadCount(int bukrs, String fromDate, String toDate, int leadId, int stageId) {

        String sql = "select count(*) from leads as a ";
        if (stageId > 0) {
            sql += " left join lead_stage_assignment as c on a.bukrs = c.bukrs and a.leadId = c.leadId and c.stats = '' ";
            sql += " left join stages_m as d on a.bukrs = d.bukrs and c.stageId = d.stageId and d.delstats = '' ";
        }
        sql += "where a.bukrs = " + bukrs + " and a.stats = '' and a.delstats = '' ";
        if (leadId > 0) {
            sql += " and a.leadId = " + leadId;
        }
        if (stageId > 0) {
            sql += " and c.stageId = " + stageId;
        }
        if (fromDate.length() > 0 && toDate.length() > 0) {
            sql += " and a.postingDate between '" + fromDate + "' and '" + toDate + "' ";
        }
        //System.out.println("sql = " + sql);
        return jdbcTempUtil.getCount(sql);
    }




    public List<struct_list_lead> getTotalLeads(int bukrs, int limit, int offset, String fromDate, String toDate, int leadId, int stageId) {

        String d = " b.descr as sourceDesc ";
        String sql = "select a.leadId, a.name as leadName, a.remarks, a.status, a.mobile, a.postingDate, a.email, a.sourceId, a.creby, c.stageId, " +
                " d.descr as stageName, a.creon as creationDate, " + d + " from leads as a ";
        sql += " left join source_m as b on a.bukrs = b.bukrs and a.sourceId = b.sourceId and b.delstats = '' ";
        sql += " left join lead_stage_assignment as c on a.bukrs = c.bukrs and a.leadId = c.leadId and c.stats = '' ";
        sql += " left join stages_m as d on a.bukrs = d.bukrs and c.stageId = d.stageId and d.delstats = '' ";
        sql += " where a.bukrs = " + bukrs + " and a.stats = '' and a.delstats = '' ";
        if (fromDate.length() > 0 && toDate.length() > 0) {
            sql += " and a.postingDate between '" + fromDate + "' and '" + toDate + "' ";
        }
        if (leadId > 0) {
            sql += " and a.leadId = " + leadId;
        }
        if (stageId > 0) {
             sql += " and c.stageId = " + stageId;
        }
        if (limit > 0) {
            sql += " LIMIT " + limit + " OFFSET " + offset;
        }
        //System.out.println("sql = " + sql);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(struct_list_lead.class));
    }





    public List<lead_cmts> getLeadComments(int bukrs) {

        String sql = "select leadId, stageId, comments as comment, creon as commentDate, commentId  from lead_comments where bukrs = " + bukrs;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(lead_cmts.class));
    }


    public Map<Integer, struct_list_lead> getMapLeads(List<struct_list_lead> list) {

        Map<Integer, struct_list_lead> info = new HashMap<>();
        info = list.stream().distinct().collect(Collectors.toMap(s -> s.getLeadId(), s -> s));
        return info;
    }

}
