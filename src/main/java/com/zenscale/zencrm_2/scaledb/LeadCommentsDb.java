package com.zenscale.zencrm_2.scaledb;

import com.zenscale.zencrm_2.structures.struct_lead_comment;
import com.zenscale.zencrm_2.utils.JdbcTempUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeadCommentsDb {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcTempUtil jdbcTempUtil;




    public int getCount(int bukrs, int leadId, int stageId, int commentId, String search) {

        String sql = "select count(*) from lead_comments where bukrs = " + bukrs;
        if (leadId > 0) {
            sql += " and leadId = " + leadId;
        }
        if (commentId > 0) {
            sql += " and commentId = " + commentId;
        }
        if (stageId > 0) {
            sql += "  and stageId = " + stageId;;
        }
        if (search.length() > 0) {
            sql += " and ( " +
                    " LOWER(comments) LIKE '%" + search + "%' " +
                    " ) ";
        }
        //System.out.println("sql = " + sql);
        return jdbcTempUtil.getCount(sql);
    }




    public List<struct_lead_comment> getRecords(int bukrs, int leadId, int stageId, int commentId, String search) {

        String sql = "select a.leadId, a.stageId, a.commentId, a.comments as comment, b.name as leadDesc, c.descr as stageDesc, a.creon as createdDate from lead_comments as a ";
        sql += " left join leads as b on a.bukrs = b.bukrs and a.leadId = b.leadId and b.stats = '' ";
        sql += " left join stages_m as c on a.bukrs = c.bukrs and a.stageId = c.stageId ";
        sql += " where a.bukrs = " + bukrs;
        if (leadId > 0) {
            sql += " and a.leadId = " + leadId;
        }
        if (commentId > 0) {
            sql += " and a.commentId = " + commentId;
        }
        if (stageId > 0) {
            sql += "  and a.stageId = " + stageId;;
        }
        if (search.length() > 0) {
            sql += " and ( " +
                    " LOWER(a.comments) LIKE '%" + search + "%' " +
                    " ) ";
        }
        sql += " ORDER BY a.creon DESC";
//        System.out.println("sql = " + sql);
        List<struct_lead_comment> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(struct_lead_comment.class));
        return list;
    }




}
