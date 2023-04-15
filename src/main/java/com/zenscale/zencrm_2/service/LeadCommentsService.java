package com.zenscale.zencrm_2.service;

import com.zenscale.zencrm_2.entity.LeadComments;
import com.zenscale.zencrm_2.entity.LeadCommentsId;
import com.zenscale.zencrm_2.repo.RepoLeadComments;
import com.zenscale.zencrm_2.scaledb.LeadCommentsDb;
import com.zenscale.zencrm_2.structures.*;
import com.zenscale.zencrm_2.utils.CommonStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeadCommentsService {

    @Autowired
    RepoLeadComments repoLeadComments;

    @Autowired
    public LeadCommentsDb leadCommentsDb;




    public struct_response_create_lead_comment createLeadComment(int bukrs, struct_body_create_lead_comment body) {

        LeadComments leadComments = new LeadComments();
        if (body.getCommentId() > 0) {
            leadComments.setId(new LeadCommentsId(bukrs, body.getCommentId()));
        } else {
            int maxCmtId = getMaxCommentId(bukrs);
            leadComments.setId(new LeadCommentsId(bukrs, maxCmtId));
        }
        leadComments.setLeadId(body.getLeadId());
        leadComments.setStageId(body.getStageId());
        leadComments.setComments(body.getComment());
        repoLeadComments.save(leadComments);

        if (body.getCommentId() > 0) {
            return new struct_response_create_lead_comment(CommonStrings.api_status_success, "Comment updated successfully", leadComments.getId().getCommentId());
        }
        return new struct_response_create_lead_comment(CommonStrings.api_status_success, "Comment created successfully", leadComments.getId().getCommentId());
    }




    public struct_response_read_lead_comment readLeadComments(int bukrs, int leadId, int stageId, int commentId, String search) {

        int totalRecords = leadCommentsDb.getCount(bukrs, leadId, stageId, commentId, search);
        List<struct_lead_comment> list = new ArrayList<>();

        if (totalRecords > 0) {
            list = leadCommentsDb.getRecords(bukrs, leadId, stageId, commentId, search);
            return new struct_response_read_lead_comment(CommonStrings.api_status_success, "Comments retrieved successfully", totalRecords, list);
        }
        return new struct_response_read_lead_comment(CommonStrings.api_status_failure, "Comments not found", totalRecords, list);
    }




    private int getMaxCommentId(int bukrs) {

        return repoLeadComments.getMaxCommentId(bukrs) + 1;
    }




    public boolean isCommentExists(int bukrs, int commentId) {

        return repoLeadComments.existsById(new LeadCommentsId(bukrs, commentId));
    }




}
