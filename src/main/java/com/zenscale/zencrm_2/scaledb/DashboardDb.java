package com.zenscale.zencrm_2.scaledb;

import com.zenscale.zencrm_2.structures.lead_cmts;
import com.zenscale.zencrm_2.structures.struct_leads_by_days;
import com.zenscale.zencrm_2.structures.struct_list_lead;
import com.zenscale.zencrm_2.structures.struct_stage_lead_count;
import com.zenscale.zencrm_2.utils.JdbcTempUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DashboardDb {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcTempUtil jdbcTempUtil;

    @Autowired
    ReportDb reportDb;




    public int getRecordCount(int bukrs, String fromDate, String toDate) {

        String sql = "SELECT COUNT(*) FROM `lead_stage_assignment` WHERE `bukrs` = " + bukrs + " AND stats = '' ";
        if (fromDate.length() > 0 && toDate.length() > 0) {
            sql += "  AND postingDate BETWEEN '" + fromDate + "' and '" + toDate + "' ";
        }
        return jdbcTempUtil.getCount(sql);
    }




    public List<struct_list_lead> getLeadsWithStages(int bukrs, String fromDate, String toDate) {

        String sql = "select a.leadId, a.name as leadName, a.remarks, a.status, a.mobile, a.postingDate, a.email, a.sourceId, a.creby, c.stageId, " +
                " d.descr as stageName, a.creon as creationDate, b.descr as sourceDesc from leads as a ";
        sql += " left join source_m as b on a.bukrs = b.bukrs and a.sourceId = b.sourceId and b.delstats = '' ";
        sql += " left join lead_stage_assignment as c on a.bukrs = c.bukrs and a.leadId = c.leadId and c.stats = '' ";
        sql += " left join stages_m as d on a.bukrs = d.bukrs and c.stageId = d.stageId and d.delstats = '' ";
        sql += " where a.bukrs = " + bukrs + " and a.stats = '' and a.delstats = '' ";
        if (fromDate.length() > 0 && toDate.length() > 0) {
            sql += "  AND a.postingDate BETWEEN '" + fromDate + "' and '" + toDate + "' ";
        }
        sql += " order by c.stageId ";

        List<struct_list_lead> listLeads = jdbcTemplate.query(sql, new BeanPropertyRowMapper(struct_list_lead.class));
        List<lead_cmts> listCmts = reportDb.getLeadComments(bukrs);
        Map<Integer, struct_list_lead> mapLead = reportDb.getMapLeads(listLeads);

        for (lead_cmts lc : listCmts) {
            if (mapLead.containsKey(lc.getLeadId())) {
                mapLead.get(lc.getLeadId()).getComments().add(lc);
            }
        }

        return listLeads;
    }




    public List<struct_stage_lead_count> getTotalLeadsByStages(int bukrs, String fromDate, String toDate) {

        String sql = "SELECT a.stageId, a.descr as stageName, a.colorCode, COUNT(c.leadId) as totalLeads FROM `stages_m` as a " +
                " LEFT JOIN lead_stage_assignment as b on a.bukrs = b.bukrs AND a.stageId = b.stageId AND b.stats = '' " +
                " LEFT JOIN leads as c ON a.bukrs = c.bukrs AND b.leadId = c.leadId AND c.stats = '' AND c.delstats = '' " +
                " WHERE a.bukrs = " + bukrs + " AND a.delstats = '' ";
        if (fromDate.length() > 0 && toDate.length() > 0) {
            sql += " AND c.postingDate BETWEEN '" + fromDate + "' and '" + toDate + "' ";
        }
        sql += " GROUP BY a.stageId;";
        //System.out.println("sql = " + sql);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(struct_stage_lead_count.class));
    }




    public List<struct_leads_by_days> getLeadsByDays(int bukrs, String fromDate, String toDate) {

        String sql = "SELECT postingDate as date, COUNT(leadId) as leadCount FROM leads WHERE bukrs = " + bukrs + " AND stats = '' AND delstats = '' ";
        if (fromDate.length() > 0 && toDate.length() > 0) {
            sql += " AND postingDate BETWEEN '" + fromDate + "' and '" + toDate + "' ";
        }
        sql += " GROUP BY postingDate ";
        sql += " order by postingDate ASC ";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(struct_leads_by_days.class));
    }




    public int getUnassignedLead(int bukrs, String fromDate, String toDate) {
        String sql = "SELECT COUNT(*) FROM `leads` as a " +
                "LEFT JOIN lead_stage_assignment as b on a.bukrs = b.bukrs AND b.stats = '' AND a.leadId = b.leadId " +
                "WHERE a.bukrs = "+bukrs+" AND a.delstats = '' AND a.stats = '' AND b.stageId is NULL ";
        if (fromDate.length() > 0 && toDate.length() > 0) {
            sql += " AND a.postingDate BETWEEN '" + fromDate + "' and '" + toDate + "' ";
        }
        return jdbcTempUtil.getCount(sql);
    }



}
