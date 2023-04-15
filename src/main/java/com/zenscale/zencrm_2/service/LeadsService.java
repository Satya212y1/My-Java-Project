package com.zenscale.zencrm_2.service;

import com.zenscale.zencrm_2.entity.LeadId;
import com.zenscale.zencrm_2.entity.Leads;
import com.zenscale.zencrm_2.entity.StngLeadAssignment;
import com.zenscale.zencrm_2.entity.StngLeadAssignmentId;
import com.zenscale.zencrm_2.repo.RepoLeads;
import com.zenscale.zencrm_2.repo.RepoStngLeadAssignment;
import com.zenscale.zencrm_2.scaledb.DashboardDb;
import com.zenscale.zencrm_2.scaledb.LeadsDb;
import com.zenscale.zencrm_2.scaledb.StageDb;
import com.zenscale.zencrm_2.structures.*;
import com.zenscale.zencrm_2.utils.CommonFunctions;
import com.zenscale.zencrm_2.utils.CommonStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class LeadsService {

    @Autowired
    private RepoLeads repoLeads;

    @Autowired
    LeadsDb leadsDb;

    @Autowired
    DashboardDb dashboardDb;

    @Autowired
    DashboardService dashboardService;

    @Autowired
    RepoStngLeadAssignment repoStngLeadAssignment;

    @Autowired
    StageDb stageDb;

    @Autowired
    CommonService commonService;

    @Autowired
    RegistrationService registrationService;

    CommonFunctions cf = new CommonFunctions();




    public struct_response_create_lead createLead(int bukrs, struct_body_create_lead body) {

        Leads leads = new Leads();
        if (body.getLeadId() > 0) {
            int maxObjid = getMaxObjid(bukrs, body.getLeadId());
            leads.setId(new LeadId(bukrs, body.getLeadId(), maxObjid));
            String status = repoLeads.getstatus(bukrs, body.getLeadId());
            leads.setStatus(status);
            updateStats(bukrs, body.getLeadId());
        } else {
            int maxLeadId = getMaxId(bukrs);
            leads.setId(new LeadId(bukrs, maxLeadId, 1));
            leads.setStatus("O");
        }
        leads.setName(body.getLeadName());
        leads.setEmail(body.getEmail());
        leads.setMobile(cf.checkNullString(body.getMobile()));
        leads.setCreby(body.getCreby());
        leads.setRemarks(cf.checkNullString(body.getRemarks()));
        leads.setSourceId(body.getSourceId());
        leads.setAssignTo(cf.checkNullString(body.getAssignTo()));
        leads.setDelstats("");
        leads.setStats("");
        leads.setPostingDate(getPostingDate(body.getPostingDate()));
        repoLeads.save(leads);

        if (body.getLeadId() > 0) {
            return new struct_response_create_lead(CommonStrings.api_status_success, "Lead updated successfully", leads.getId().getLeadId());
        }
        return new struct_response_create_lead(CommonStrings.api_status_success, "Lead created successfully", leads.getId().getLeadId());
    }




    public struct_response_delete_lead deleteLead(int bukrs, int leadid) {

        repoLeads.updateDelStats(bukrs, leadid);
        return new struct_response_delete_lead(CommonStrings.api_status_success, "Lead deleted successfully");
    }




    public struct_response_read_lead readLeads(int bukrs, int leadid, int status, int sourceId, String search, int limit, int offset, int stageId, String fromDate, String toDate, String date, String s, String stageStatus, String postingDate, String token) {

        int totalRecords = leadsDb.getTotalLeadCount(bukrs, leadid, status, sourceId, search, stageId, fromDate, toDate, stageStatus, postingDate);
        List<struct_list_lead> list = new ArrayList<>();

        if (totalRecords > 0) {
            list = leadsDb.getTotalLeads(bukrs, leadid, status, sourceId, search, limit, offset, stageId, fromDate, toDate, stageStatus, postingDate);
            Map<String, sl_users> usersMap = registrationService.getMapUsers(token);
            //System.out.println("usersMap = " + usersMap);
            for (struct_list_lead sa : list) {
                String uid = new CommonFunctions().checkNullString(sa.getAssignTo());
                if (usersMap.containsKey(uid)) {
                    sa.setAssignToUname(usersMap.get(sa.getAssignTo()).getName());
                } else {
                    sa.setAssignToUname("");
                }
            }
            return new struct_response_read_lead(CommonStrings.api_status_success, "Leads retrieved successfully", totalRecords, list);
        }
        return new struct_response_read_lead(CommonStrings.api_status_failure, "No record found", totalRecords, list);
    }




    public struct_response_assign_lead assignSourceToUser(int bukrs, struct_assign_lead_body body) {

        StngLeadAssignment stngLeadAssignment = new StngLeadAssignment();
        stngLeadAssignment.setId(new StngLeadAssignmentId(bukrs, body.getSourceId()));
        stngLeadAssignment.setUserId(body.getUserId());
        repoStngLeadAssignment.save(stngLeadAssignment);
        return new struct_response_assign_lead(CommonStrings.api_status_success, "Source successfully assigned to user");
    }




    public struct_response_leads_by_stages_kanban getLeadsByStages(int bukrs, String fromDate, String toDate, String token) {
        //System.out.println("token2 = " + token);
        int totalRecords = dashboardDb.getRecordCount(bukrs, fromDate, toDate);
        List<struct_leadsinfo_by_stage> leadsByStage = new ArrayList<>();


        List<struct_list_lead> listLeads = leadsDb.getLeadsWithStages(bukrs, fromDate, toDate);
        List<struct_list_lead> listUnassigned = leadsDb.getTotalLeads(bukrs, 0, 0, 0, "", 0, 0, -1, "",
                "", "", "");
        listLeads.addAll(listUnassigned);
        List<struct_stages> listAllStages = new ArrayList<>();
        listAllStages.add(new struct_stages(0, "Unassigned Leads", "", ""));
        listAllStages.addAll(stageDb.getTotalRecordsStages(bukrs, 0, ""));
        commonService.orderStages(listAllStages);

        Map<String, sl_users> usersMap = registrationService.getMapUsers(token);

        /* Stage wise leads */
        for (int i = 0; i < listAllStages.size(); i++) {
            int count = 0;
            struct_leadsinfo_by_stage item = new struct_leadsinfo_by_stage();
            item.setStageId(listAllStages.get(i).getStageId());
            item.setStageName(listAllStages.get(i).getDescr());
            item.setColorCode(listAllStages.get(i).getColorCode());
            for (int j = 0; j < listLeads.size(); j++) {
                if (listAllStages.get(i).getStageId() == listLeads.get(j).getStageId()) {
                    count += 1;
                    String uid = new CommonFunctions().checkNullString(listLeads.get(j).getAssignTo());
                    if (usersMap.containsKey(uid)) {
                        listLeads.get(j).setAssignToUname(usersMap.get(listLeads.get(j).getAssignTo()).getName());
                    } else {
                        listLeads.get(j).setAssignToUname("");
                        listLeads.get(j).setAssignTo("");
                    }
                    item.getListLeads().add(listLeads.get(j));
                }
            }
            item.setTotalLeads(count);
            leadsByStage.add(item);
        }

        for (struct_leadsinfo_by_stage a : leadsByStage) {
            commonService.orderLeadsByDateDescending(a.getListLeads());
        }

        if (listLeads.size() > 0) {
            return new struct_response_leads_by_stages_kanban(CommonStrings.api_status_success, "Records retrieved successfully", leadsByStage);
        }
        return new struct_response_leads_by_stages_kanban(CommonStrings.api_status_failure, "No record found", leadsByStage);
    }




    public Date getPostingDate(String datePosting) {

        Date postingDate = null;
        if (cf.checkNullString(datePosting).length() == 0) {
            postingDate = cf.getCurrentDate(CommonStrings.dateformat_hyphen_yyyyMMdd);
        } else {
            postingDate = cf.getFromattedDate(CommonStrings.dateformat_hyphen_yyyyMMdd, datePosting);
        }
        return postingDate;
    }




    private int getMaxId(int bukrs) {

        return repoLeads.getMaxId(bukrs) + 1;
    }




    private int getMaxObjid(int bukrs, int leadid) {

        return repoLeads.getMaxObjid(bukrs, leadid) + 1;
    }




    public boolean isLeadExist(int bukrs, int leadid) {

        return repoLeads.existsById_BukrsAndId_LeadIdAndDelstatsAndStats(bukrs, leadid, "", "");
    }




    public Leads findLeadExist(int bukrs, int leadid) {

        return repoLeads.findById_BukrsAndId_LeadIdAndDelstatsAndStats(bukrs, leadid, "", "");
    }




    private void updateStats(int bukrs, int leadid) {

        repoLeads.updateStats(bukrs, leadid);
    }




    public void updateStatus(int bukrs, int leadId, String status) {
        repoLeads.updateStatus(bukrs, leadId, status);
    }




}
