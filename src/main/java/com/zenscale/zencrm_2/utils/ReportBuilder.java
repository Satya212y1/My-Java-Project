package com.zenscale.zencrm_2.utils;

import com.zenscale.zencrm_2.structures.struct_layout;
import com.zenscale.zencrm_2.structures.struct_rpt_layout;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ReportBuilder {


    public struct_rpt_layout get_report_info(List<struct_layout> layout, JSONArray data) {

        HashMap<Integer, HashMap<Integer, Object>> item = new HashMap<>();
        HashMap<Integer, Object> head_pos = new HashMap<>();
        HashMap<Integer, Object> head_width = new HashMap<>();

        for (struct_layout sl : layout) {
            if (sl.getHide().equals("X")) {
                continue;
            }
            head_pos.put(sl.getPos(), sl.getColval());
            head_width.put(sl.getPos(), sl.getWidth());
        }

        for (int i = 0; i < data.length(); i++) {

            JSONObject j = data.getJSONObject(i);
            HashMap<Integer, Object> it = new HashMap<Integer, Object>();

            for (struct_layout sl : layout) {
                if (sl.getHide().equals("X")) {
                    continue;
                }
                Object colval = j.has(sl.getColfield()) ? j.get(sl.getColfield()) : "";
                if (colval instanceof JSONArray) {
                    JSONArray ar = new JSONArray(j.get(sl.getColfield()).toString());
                    colval = ar.toList();
                } else if (colval instanceof JSONObject) {
                    JSONObject ar = new JSONObject(j.get(sl.getColfield()).toString());
                    colval = ar.toMap();
                }
                int pos = sl.getPos();
                it.put(pos, colval);
            }
            item.put(i, it);
        }

        Collections.sort(layout);
        struct_rpt_layout srl = new struct_rpt_layout();
        srl.setHead_pos(head_pos);
        srl.setHead_width(head_width);
        srl.setItem(item);
        srl.setLayout(layout);

        return srl;
    }




}
