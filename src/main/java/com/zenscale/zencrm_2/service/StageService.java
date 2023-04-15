package com.zenscale.zencrm_2.service;

import com.zenscale.zencrm_2.entity.StagesM;
import com.zenscale.zencrm_2.entity.StagesMId;
import com.zenscale.zencrm_2.repo.RepoLeadStageAssignment;
import com.zenscale.zencrm_2.repo.RepoStagesM;
import com.zenscale.zencrm_2.scaledb.StageDb;
import com.zenscale.zencrm_2.structures.*;
import com.zenscale.zencrm_2.utils.CommonFunctions;
import com.zenscale.zencrm_2.utils.CommonStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StageService {

    @Autowired
    RepoStagesM repoStagesM;

    @Autowired
    RepoLeadStageAssignment repoLeadStageAssignment;

    @Autowired
    StageDb stageDb;

    @Autowired
    LeadsService leadsService;

    CommonFunctions cf = new CommonFunctions();




    public struct_response_create_stage createStage(int bukrs, struct_body_create_stage body) {

        StagesM stagesM = new StagesM();
        if (body.getStageId() > 0) {
            stagesM.setId(new StagesMId(bukrs, body.getStageId()));
        } else {
            int stageId = getMaxStageId(bukrs);
            stagesM.setId(new StagesMId(bukrs, stageId));
        }
        stagesM.setDescr(body.getDescr());
        stagesM.setStatus(body.getStatus());
        stagesM.setColorCode(cf.checkNullString(body.getColorCode()));
        stagesM.setDelstats("");
        repoStagesM.save(stagesM);

        List<Integer> leadIdList = repoLeadStageAssignment.findLeadIdByStageId(bukrs, stagesM.getId().getStageId());
        if (leadIdList != null && leadIdList.size() > 0) {
            for (int i = 0; i < leadIdList.size(); i++) {
                int leadId = leadIdList.get(i).intValue();
                if (leadId > 0) {
                    if (stagesM.getStatus().equals("O")) {
                        leadsService.updateStatus(bukrs, leadId, "I");
                    } else if (stagesM.getStatus().equals("C")) {
                        leadsService.updateStatus(bukrs, leadId, "C");
                    }
                }
            }
        }

        if (body.getStageId() > 0) {
            return new struct_response_create_stage(CommonStrings.api_status_success, "Stage updated successfully", stagesM.getId().getStageId());
        }
        return new struct_response_create_stage(CommonStrings.api_status_success, "Stage created successfully", stagesM.getId().getStageId());
    }




    public struct_response_read_stage readStages(int bukrs, int stageid, String search) {

        int totalRecords = stageDb.getTotalRecordsStagesCount(bukrs, stageid, search);
        List<struct_stages> list = new ArrayList<>();

        if (totalRecords > 0) {
            list = stageDb.getTotalRecordsStages(bukrs, stageid, search);
            return new struct_response_read_stage(CommonStrings.api_status_success, "Stages retrieved successfully", totalRecords, list);
        }
        return new struct_response_read_stage(CommonStrings.api_status_failure, "No record found", totalRecords, list);
    }




    public struct_response_delete_stage deleteStages(int bukrs, int stageid) {

        repoStagesM.updateDelStats(bukrs, stageid);
        return new struct_response_delete_stage(CommonStrings.api_status_success, "Stage deleted successfully");
    }




    public boolean validateStageStatus(String status) {

        List<String> list = new ArrayList<>();
        list.add("O");
        list.add("C");
        if (list.contains(status)) {
            return true;
        }
        return false;
    }




    private int getMaxStageId(int bukrs) {

        return repoStagesM.getMaxStageId(bukrs) + 1;
    }




    public boolean isStageExist(int bukrs, int stageid) {

        return repoStagesM.existsById_BukrsAndId_StageIdAndDelstats(bukrs, stageid, "");
    }




    public StagesM findStageByBukrsAndStageId(int bukrs, int stageid) {

        return repoStagesM.findById_BukrsAndId_StageIdAndDelstats(bukrs, stageid, "");
    }




    public StagesM checkStageDescExist(int bukrs, String descr) {

        return repoStagesM.findById_BukrsAndDescrAndDelstats(bukrs, descr, "");
    }




    public String findStageStatus(int bukrs, int stageId) {
        return repoStagesM.findStatus(bukrs, stageId);
    }




}
