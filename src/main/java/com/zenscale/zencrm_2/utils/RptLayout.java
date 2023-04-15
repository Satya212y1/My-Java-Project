package com.zenscale.zencrm_2.utils;

import com.google.gson.Gson;
import com.zenscale.zencrm_2.structures.struct_layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RptLayout {


    private String url;
    private String user;
    private String pass;




    public RptLayout(String url, String user, String pass) {

        this.url = url;
        this.user = user;
        this.pass = pass;
    }




    public List<struct_layout> get_layout(int bukrs, String uid, int reportId) {

        List<struct_layout> data = new ArrayList<>();
        if (bukrs > 0 && uid.length() > 0) {
            data = get_client_layout(bukrs, uid, reportId);
        }

        if (data == null||data.size()<=0) {
            data = get_default_layout(reportId);

        }
        return data;
    }




    private List<struct_layout> get_default_layout(int rptid) {

        String query = "Select  * from  layout_default where rptid = " + rptid;
        return execute_query(query);
    }




    private List<struct_layout> get_client_layout(int bukrs, String uid, int rptid) {

        String query = "Select  a.* , b.fieldType from  layout_client as a " +
                " left join layout_default as b on a.rptid = b.rptid " +
                " and a.colid = b.colid "+
                " where a.rptid = " + rptid;
        if (bukrs > 0) {
            query += " and bukrs=" + bukrs;
        }
        if (uid.length() > 0) {
            query += " and uid ='" + uid + "'";
        }

        return execute_query(query);
    }




    private List<struct_layout> execute_query(String query) {

        db d = new db(url, user, pass);
        List<Map<String, Object>> obj = d.get(query);
        if (obj == null) {
            return null;
        }
        List<struct_layout> data = new ArrayList<struct_layout>();
        for (Map<String, Object> a : obj) {
            Gson gson = new Gson();
            String x = gson.toJson(a);
            struct_layout rec = gson.fromJson(x, struct_layout.class);
            data.add(rec);
        }
        return data;
    }




}
