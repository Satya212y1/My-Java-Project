package com.zenscale.zencrm_2.api;

import com.zenscale.zencrm_2.entity.LayoutIdentity;
import com.zenscale.zencrm_2.entity.layout;
import com.zenscale.zencrm_2.repo.RepoLayout;
import com.zenscale.zencrm_2.scaledb.LeadsDb;
import com.zenscale.zencrm_2.service.AuthService;
import com.zenscale.zencrm_2.service.ReportService;
import com.zenscale.zencrm_2.structures.*;
import com.zenscale.zencrm_2.utils.*;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/crm/report/")
public class ReportController {


    @Autowired
    AuthService authService;

    @Autowired
    dbService dbService;

    @Autowired
    LeadsDb leadsDb;

    @Autowired
    ReportService reportService;

    @Autowired
    RepoLayout repoLayout;

    CommonFunctions cf = new CommonFunctions();




    @ResponseBody
    @PostMapping("save_layout")
    public struct_response savelayout(@RequestHeader(name = "Authorization", required = false) String token,
                                      @RequestBody struct_layout_post body) {

        String uid = body.getUid();
        List<struct_layout> values = body.getLayout();

        if (token.equals("")) {
            return new struct_response(CommonStrings.api_status_failure, "Authorization token missing !");
        }


        int bukrs = authService.get_bukrs_token(token);
        if (bukrs <= 0) {
            return new struct_response(CommonStrings.api_status_failure, "Invalid token");
        }

        ArrayList<layout> arrayList = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            LayoutIdentity rptIdentity = new LayoutIdentity();
            rptIdentity.setRptid(values.get(i).getRptid());
            rptIdentity.setColid(values.get(i).getColid());
            rptIdentity.setUid(uid);
            rptIdentity.setBukrs(bukrs);
            layout structLayoutEn = new layout();
            structLayoutEn.setLayoutIdentity(rptIdentity);
            structLayoutEn.setColfield(values.get(i).getColfield());
            structLayoutEn.setPos(values.get(i).getPos());
            structLayoutEn.setColval(values.get(i).getColval());
            structLayoutEn.setWidth(values.get(i).getWidth());
            if (cf.checkNullString(values.get(i).getHide()).equals("")) {
                structLayoutEn.setHide("");
            } else {
                if (values.get(i).getHide().equals("null")) {
                    structLayoutEn.setHide("");
                } else {
                    structLayoutEn.setHide(values.get(i).getHide());
                }

            }
            arrayList.add(structLayoutEn);
        }

        repoLayout.saveAll(arrayList);
        return new struct_response(1, "Saved Successfully");
    }




    @GetMapping("leads")
    public struct_rpt_layout getReportLead(@RequestHeader(name = "Authorization", required = false) String token,
                                                       @RequestParam(required = false, defaultValue = "") String uid,
                                                       @RequestParam(name = "limit", required = false, defaultValue = "0") int limit,
                                                       @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
                                                       @RequestParam(name = "fromDate", required = false, defaultValue = "") String fromDate,
                                                       @RequestParam(name = "toDate", required = false, defaultValue = "") String toDate,
                                                       @RequestParam(name = "leadId", required = false, defaultValue = "0") int leadId,
                                                       @RequestParam(name = "stageId", required = false, defaultValue = "0") int stageId) {

        int reportId = 1001;

        if (token == null || token.equals("")) {
            return new struct_rpt_layout(CommonStrings.api_status_failure, "Authorization token missing !");
        }

        int bukrs = authService.get_bukrs_token(token);
        if (bukrs == 0) {
            return new struct_rpt_layout(CommonStrings.api_status_failure, "Invalid token");
        }

        if (fromDate.length() > 0 && toDate.length() > 0) {
            if (!cf.isDateFormatted(CommonStrings.dateformat_hyphen_yyyyMMdd, fromDate)) {
                return new struct_rpt_layout(CommonStrings.api_status_failure, "Invalid From-Date format ! Valid From-Date format is yyyy-MM-dd");
            }
            if (!cf.isDateFormatted(CommonStrings.dateformat_hyphen_yyyyMMdd, toDate)) {
                return new struct_rpt_layout(CommonStrings.api_status_failure, "Invalid To-Date format ! Valid To-Date format is yyyy-MM-dd");
            }
            /*if (!cf.isToDateGreaterThanFromDate(fromDate, toDate, CommonStrings.dateformat_hyphen_yyyyMMdd)) {
                return new struct_rpt_layout(CommonStrings.api_status_failure, "From-Date must occur before To-Date");
            }*/
        }

        ReportBuilder rb = new ReportBuilder();
        RptLayout l = new RptLayout(dbService.get_url(), dbService.get_user(), dbService.get_password());
        List<struct_layout> layout = l.get_layout(bukrs, uid, reportId);
        List<struct_list_lead> data = new ArrayList<>();

        int count = 0;
        data = reportService.getTotalLeads(bukrs, limit, offset, fromDate, toDate, leadId, stageId);
        count = reportService.getTotalLeadCount(bukrs, fromDate, toDate, leadId, stageId);

        struct_rpt_layout s = new struct_rpt_layout();
        if (layout != null && data != null) {
            s = rb.get_report_info(layout, new JSONArray(data));
            s.setTotalRecords(count);
        }

        return s;
    }




}
