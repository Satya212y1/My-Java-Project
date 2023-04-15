package com.zenscale.zencrm_2.service;

import com.zenscale.zencrm_2.entity.LeadStageAssignment;
import com.zenscale.zencrm_2.entity.LeadStageAssignmentId;
import com.zenscale.zencrm_2.repo.RepoLeadStageAssignment;
import com.zenscale.zencrm_2.scaledb.LeadStageAssignmentDb;
import com.zenscale.zencrm_2.structures.struct_body_lead_assignment;
import com.zenscale.zencrm_2.structures.struct_lead_assignment;
import com.zenscale.zencrm_2.structures.struct_response_lead_assignment;
import com.zenscale.zencrm_2.structures.struct_response_lead_assignment_read;
import com.zenscale.zencrm_2.utils.CommonStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeadStageAssignmentService {

    @Autowired
    RepoLeadStageAssignment repoLeadStageAssignment;

    @Autowired
    LeadStageAssignmentDb leadStageAssignmentDb;

    @Autowired
    public LeadsService leadsService;

    @Autowired
    public StageService stageService;





    public struct_response_lead_assignment createLeadAssignment(int bukrs, struct_body_lead_assignment body) {

        struct_response_lead_assignment response = null;
        LeadStageAssignment leadStageAssignment = new LeadStageAssignment();

        if (isRecordExist(bukrs, body.getLeadId())) {
            int maxObjid = getMaxObjid(bukrs, body.getLeadId());
            updateStats(bukrs, body.getLeadId());
            leadStageAssignment.setId(new LeadStageAssignmentId(bukrs, body.getLeadId(), maxObjid));
            response = new struct_response_lead_assignment(CommonStrings.api_status_success, "Record updated successfully");
        } else {
            leadStageAssignment.setId(new LeadStageAssignmentId(bukrs, body.getLeadId(), 1));
            response = new struct_response_lead_assignment(CommonStrings.api_status_success, "Record created successfully");
        }

        leadStageAssignment.setStageId(body.getStageId());
        leadStageAssignment.setStats("");
        repoLeadStageAssignment.save(leadStageAssignment);

        String status = stageService.findStageStatus(bukrs, body.getStageId());
        //System.out.println("status = " + status);
        if (status.equals("O")) {
            leadsService.updateStatus(bukrs, body.getLeadId(), "I");
        } else if (status.equals("C")){
            leadsService.updateStatus(bukrs, body.getLeadId(), "C");
        }


        return response;
    }




    public struct_response_lead_assignment_read readLeadStageAssignment(int bukrs, int stageId, int leadId) {

        int totalRecords = leadStageAssignmentDb.getCount(bukrs, stageId, leadId);
        List<struct_lead_assignment> list = new ArrayList<>();
        List<struct_lead_assignment> data = new ArrayList<>();
        List<struct_lead_assignment> dataHistory = new ArrayList<>();

        if (totalRecords > 0) {
            list = leadStageAssignmentDb.getRecords(bukrs, stageId, leadId);

            for (struct_lead_assignment item: list) {
                if (item.getStats().equals("X")) {
                    dataHistory.add(item);
                } else {
                    data.add(item);
                }
            }

            return new struct_response_lead_assignment_read(CommonStrings.api_status_success, "Records retrieved successfully", totalRecords, data, dataHistory);
        }
        return new struct_response_lead_assignment_read(CommonStrings.api_status_failure, "No record found", totalRecords, data, dataHistory);
    }




    public boolean isRecordExist(int bukrs, int leadid) {

        return repoLeadStageAssignment.existsById_BukrsAndId_LeadIdAndStats(bukrs, leadid, "");
    }


    public boolean isRecordExistByLeadIdAndStageId(int bukrs, int leadid, int stageId) {

        return repoLeadStageAssignment.existsById_BukrsAndId_LeadIdAndStageIdAndStats(bukrs, leadid, stageId,"");
    }






    private void updateStats(int bukrs, int leadid) {

        repoLeadStageAssignment.updateStats(bukrs, leadid);
    }




    private int getMaxObjid(int bukrs, int leadid) {

        return repoLeadStageAssignment.getMaxObjid(bukrs, leadid) + 1;
    }




}
