package com.zenscale.zencrm_2.api;

import com.zenscale.zencrm_2.service.AuthService;
import com.zenscale.zencrm_2.service.DashboardService;
import com.zenscale.zencrm_2.structures.struct_response_leads_by_days;
import com.zenscale.zencrm_2.structures.struct_response_leads_by_stages;
import com.zenscale.zencrm_2.utils.CommonFunctions;
import com.zenscale.zencrm_2.utils.CommonStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crm/dashboard/")
public class DashboardController {

    @Autowired
    AuthService authService;

    @Autowired
    DashboardService dashboardService;

    CommonFunctions cf = new CommonFunctions();




    @GetMapping("leads_by_stages")
    public struct_response_leads_by_stages leadsByStages(@RequestHeader(value = "Authorization", required = false, defaultValue = "") String token,
                                                         @RequestParam(name = "fromDate", required = false, defaultValue = "") String fromDate,
                                                         @RequestParam(name = "toDate", required = false, defaultValue = "") String toDate) {

        if (token.length() == 0) {
            return new struct_response_leads_by_stages(CommonStrings.api_status_failure, "Authorization missing");
        }
        int bukrs = authService.get_bukrs_token(token);
        if (bukrs == 0) {
            return new struct_response_leads_by_stages(CommonStrings.api_status_failure, "Invalid authorization token");
        }

        if (fromDate.length() > 0 && toDate.length() > 0) {
            if (!cf.isDateFormatted(CommonStrings.dateformat_hyphen_yyyyMMdd, fromDate)) {
                return new struct_response_leads_by_stages(CommonStrings.api_status_failure, "Invalid From-Date format ! Valid From-Date format is yyyy-MM-dd");
            }
            if (!cf.isDateFormatted(CommonStrings.dateformat_hyphen_yyyyMMdd, toDate)) {
                return new struct_response_leads_by_stages(CommonStrings.api_status_failure, "Invalid To-Date format ! Valid To-Date format is yyyy-MM-dd");
            }
            /*if (!cf.isToDateGreaterThanFromDate(fromDate, toDate, CommonStrings.dateformat_hyphen_yyyyMMdd)) {
                return new struct_response_leads_by_stages(CommonStrings.api_status_failure, "From-Date must occur before To-Date");
            }*/
        }

        return dashboardService.getLeadsByStages(bukrs, fromDate, toDate);
    }




    @GetMapping("leads_by_days")
    public struct_response_leads_by_days leadsByDays(@RequestHeader(value = "Authorization", required = false, defaultValue = "") String token,
                                                     @RequestParam(name = "fromDate", required = false, defaultValue = "") String fromDate,
                                                     @RequestParam(name = "toDate", required = false, defaultValue = "") String toDate) {

        if (token.length() == 0) {
            return new struct_response_leads_by_days(CommonStrings.api_status_failure, "Authorization missing");
        }
        int bukrs = authService.get_bukrs_token(token);
        if (bukrs == 0) {
            return new struct_response_leads_by_days(CommonStrings.api_status_failure, "Invalid authorization token");
        }

        if (cf.checkNullString(fromDate).length() == 0) {
            return new struct_response_leads_by_days(CommonStrings.api_status_failure, "From date missing");
        }

        if (cf.checkNullString(toDate).length() == 0) {
            return new struct_response_leads_by_days(CommonStrings.api_status_failure, "To date missing");
        }

        if (fromDate.length() > 0 && toDate.length() > 0) {
            if (!cf.isDateFormatted(CommonStrings.dateformat_hyphen_yyyyMMdd, fromDate)) {
                return new struct_response_leads_by_days(CommonStrings.api_status_failure, "Invalid From-Date format ! Valid From-Date format is yyyy-MM-dd");
            }
            if (!cf.isDateFormatted(CommonStrings.dateformat_hyphen_yyyyMMdd, toDate)) {
                return new struct_response_leads_by_days(CommonStrings.api_status_failure, "Invalid To-Date format ! Valid To-Date format is yyyy-MM-dd");
            }
            /*if (!cf.isToDateGreaterThanFromDate(fromDate, toDate, CommonStrings.dateformat_hyphen_yyyyMMdd)) {
                return new struct_response_leads_by_days(CommonStrings.api_status_failure, "From-Date must occur before To-Date");
            }*/
        }

        return dashboardService.getLeadsByDays(bukrs, fromDate, toDate);
    }




}
