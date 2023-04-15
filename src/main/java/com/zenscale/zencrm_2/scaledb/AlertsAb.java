package com.zenscale.zencrm_2.scaledb;

import com.zenscale.zencrm_2.structures.struct_alert_users;
import com.zenscale.zencrm_2.structures.struct_alerts;
import com.zenscale.zencrm_2.structures.struct_email_alert;
import com.zenscale.zencrm_2.utils.CommonStrings;
import com.zenscale.zencrm_2.utils.JdbcTempUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertsAb {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcTempUtil jdbcTempUtil;




    public List<struct_alerts> findAllAlerts(int bukrs, int alertId, int alertCounter) {

        String sql = "SELECT a.*, b.docId, b.docType, b.creby, c.leadId, d.name as leadDesc, e.descr as stageDesc, c.stageId, c.comments FROM `alerts_i` as a " +
                " LEFT JOIN alerts_h as b ON a.bukrs = b.bukrs AND a.alertId = b.alertId " +
                " LEFT JOIN lead_comments as c ON a.bukrs = c.bukrs AND b.docId = c.commentId"+
                " LEFT JOIN leads as d ON a.bukrs = d.bukrs AND c.leadId = d.leadId AND d.stats = '' "+
                " LEFT JOIN stages_m as e ON a.bukrs = e.bukrs AND c.stageId = e.stageId "+
                " WHERE a.bukrs = " + bukrs;
        if (alertId > 0) {
            sql += " and a.alertId = " + alertId;
        }
        if (alertCounter > 0) {
            sql += " and a.counter = " + alertCounter;
        }
        sql += " ORDER BY a.alertId;";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(struct_alerts.class));
    }




    public List<struct_alert_users> findAlertUsers(int bukrs) {

        String sql = "select alertId, counter, uid from alerts_assign where bukrs = " + bukrs + " order by alertId";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(struct_alert_users.class));
    }




    public List<struct_email_alert> getAlertListForEmail(Long currentMillis) {
        String sql = "SELECT a.bukrs, a.alertId, a.counter, a.executionTime, a.status, b.uid, c.docId, c.docType, c.creby FROM `alerts_i` AS a " +
                " LEFT JOIN alerts_assign as b ON a.bukrs = b.bukrs AND a.alertId = b.alertId  AND a.counter = b.alertCounter " +
                " LEFT JOIN alerts_h AS c ON a.bukrs = c.bukrs AND a.alertId = c.alertId " +
                " WHERE a.status = 'A' AND b.uid IS NOT NULL and a.executionTimeStamp < " + currentMillis;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(struct_email_alert.class));
    }




}
