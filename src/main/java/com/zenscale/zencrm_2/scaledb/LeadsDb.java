package com.zenscale.zencrm_2.scaledb;

import com.zenscale.zencrm_2.service.RegistrationService;
import com.zenscale.zencrm_2.structures.lead_cmts;
import com.zenscale.zencrm_2.structures.sl_users;
import com.zenscale.zencrm_2.structures.struct_list_lead;
import com.zenscale.zencrm_2.utils.CommonFunctions;
import com.zenscale.zencrm_2.utils.CommonStrings;
import com.zenscale.zencrm_2.utils.JdbcTempUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LeadsDb {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcTempUtil jdbcTempUtil;

    @Autowired
    ReportDb reportDb;

    @Autowired
    RegistrationService registrationService;




    public int getTotalLeadCount(int bukrs, int leadid, int status, int sourceId, String search, int stageId, String fromDate, String toDate,
                                 String stageStatus, String postingDate) {

        String sql = "select count(*) from leads as a ";
        sql += " left join source_m as b on a.bukrs = b.bukrs and a.sourceId = b.sourceId and b.delstats = '' ";
        sql += " left join lead_stage_assignment as c on a.bukrs = c.bukrs and a.leadId = c.leadId and c.stats = '' ";
        sql += " left join stages_m as d on a.bukrs = d.bukrs and c.stageId = d.stageId and d.delstats = '' ";
        sql += " where a.bukrs = " + bukrs + " and a.stats = '' and a.delstats = '' ";
        if (leadid > 0) {
            sql += " and a.leadId = " + leadid;
        } else if (leadid == 0 && search.length() > 0) {
            sql += " and ( " +
                    " LOWER(a.name) LIKE '%" + search + "%' OR " +
                    " LOWER(a.remarks) LIKE '%" + search + "%' OR " +
                    " a.mobile LIKE '%" + search + "%' OR " +
                    " a.email LIKE '%" + search + "%' OR " +
                    " a.sourceId LIKE '%" + search + "%' OR " +
                    " LOWER(a.creby) LIKE '%" + search + "%' " +
                    " ) ";
        }
        if (status > 0) {
            sql += " and a.status = " + status;
        }
        if (sourceId > 0) {
            sql += " and a.sourceId = " + sourceId;
        }
        if (postingDate.length() > 0) {
            sql += " and a.postingDate = '" + postingDate + "'";
        }
        if (stageId > 0) {
            sql += " and c.stageId = " + stageId;
        }
        if (stageId == -1) {
            sql += " and c.stageId is NULL ";
        }
        if (stageStatus.equals("O")) {
            sql += " and d.status = '"+stageStatus+"' ";
        } else if (stageStatus.equals("C")) {
            sql += " and d.status = '"+stageStatus+"' ";
        }
        if (fromDate.length() > 0 && toDate.length() > 0) {
            sql += " and a.postingDate between '" + fromDate + "' and '" + toDate + "' ";
        }

        //System.out.println("sql = " + sql);
        return jdbcTempUtil.getCount(sql);
    }




    public List<struct_list_lead> getTotalLeads(int bukrs, int leadid, int status, int sourceId, String search, int limit, int offset,
                                                int stageId, String fromDate, String toDate, String stageStatus, String postingDate) {

        String sql = "select a.leadId, a.assignTo, a.name as leadName, a.remarks, a.status, a.mobile, a.postingDate, a.email, a.sourceId, a.creby, c.stageId, " +
                " coalesce(d.descr, '') as stageName, a.creon as creationDate, b.descr as sourceDesc from leads as a ";
        sql += " left join source_m as b on a.bukrs = b.bukrs and a.sourceId = b.sourceId and b.delstats = '' ";
        sql += " left join lead_stage_assignment as c on a.bukrs = c.bukrs and a.leadId = c.leadId and c.stats = '' ";
        sql += " left join stages_m as d on a.bukrs = d.bukrs and c.stageId = d.stageId and d.delstats = '' ";
        sql += " where a.bukrs = " + bukrs + " and a.stats = '' and a.delstats = '' ";
        if (leadid > 0) {
            sql += " and a.leadId = " + leadid;
        } else if (leadid == 0 && search.length() > 0) {
            sql += " and ( " +
                    " LOWER(a.name) LIKE '%" + search + "%' OR " +
                    " LOWER(a.remarks) LIKE '%" + search + "%' OR " +
                    " a.mobile LIKE '%" + search + "%' OR " +
                    " a.email LIKE '%" + search + "%' OR " +
                    " a.sourceId LIKE '%" + search + "%' OR " +
                    " LOWER(a.creby) LIKE '%" + search + "%' " +
                    " ) ";
        }
        if (status > 0) {
            sql += " and a.status = " + status;
        }
        if (sourceId > 0) {
            sql += " and a.sourceId = " + sourceId;
        }
        if (stageId > 0) {
            sql += " and c.stageId = " + stageId;
        }
        if (stageId == -1) {
            sql += " and c.stageId is NULL ";
        }
        if (fromDate.length() > 0 && toDate.length() > 0) {
            sql += " and a.postingDate between '" + fromDate + "' and '" + toDate + "' ";
        }
        if (postingDate.length() > 0) {
            sql += " and a.postingDate = '" + postingDate + "'";
        }
        if (stageStatus.equals("O")) {
            sql += " and d.status = 'O' ";
        } else if (stageStatus.equals("C")) {
            sql += " and d.status = 'C' ";
        }
        sql += " ORDER BY a.creon DESC ";
        if (limit > 0) {
            sql += " LIMIT " + limit + " OFFSET " + offset;
        }
        System.out.println("sql = " + sql);
        List<struct_list_lead> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(struct_list_lead.class));
        List<lead_cmts> listCmts = reportDb.getLeadComments(bukrs);
        Map<Integer, struct_list_lead> mapLead = reportDb.getMapLeads(list);

        for (lead_cmts lc : listCmts) {
            if (mapLead.containsKey(lc.getLeadId())) {
                mapLead.get(lc.getLeadId()).getComments().add(lc);
            }
        }
        return list;
    }




    public List<struct_list_lead> getLeadsWithStages(int bukrs, String fromDate, String toDate) {

        String sql = "select a.leadId, a.assignTo, a.name as leadName, a.remarks, a.status, a.mobile, a.postingDate, a.email, a.sourceId, a.creby, c.stageId, " +
                " d.descr as stageName, a.creon as creationDate, b.descr as sourceDesc, d.status as stageStatus from leads as a ";
        sql += " left join source_m as b on a.bukrs = b.bukrs and a.sourceId = b.sourceId and b.delstats = '' ";
        sql += " left join lead_stage_assignment as c on a.bukrs = c.bukrs and a.leadId = c.leadId and c.stats = '' ";
        sql += " left join stages_m as d on a.bukrs = d.bukrs and c.stageId = d.stageId and d.delstats = '' ";
        sql += " where a.bukrs = " + bukrs + " and a.stats = '' and a.delstats = '' ";
        if (fromDate.length() > 0 && toDate.length() > 0) {
            sql += "  AND a.postingDate BETWEEN '" + fromDate + "' and '" + toDate + "' ";
        }
        sql += " AND c.stageId is NOT NULL ";
        //sql += " order by c.stageId ";
//        System.out.println("sql1 = " + sql);
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




}
