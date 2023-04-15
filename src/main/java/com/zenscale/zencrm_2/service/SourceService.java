package com.zenscale.zencrm_2.service;

import com.zenscale.zencrm_2.entity.SourceM;
import com.zenscale.zencrm_2.entity.SourceMId;
import com.zenscale.zencrm_2.repo.RepoSourceM;
import com.zenscale.zencrm_2.scaledb.SourceDb;
import com.zenscale.zencrm_2.structures.*;
import com.zenscale.zencrm_2.utils.CommonStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SourceService {


    @Autowired
    RepoSourceM repoSourceM;

    @Autowired
    SourceDb sourceDb;




    private int getMaxSourceId(int bukrs) {

        return repoSourceM.getMaxSourceId(bukrs) + 1;
    }




    public boolean isSourceExist(int bukrs, int sourceid) {

        return repoSourceM.existsById_BukrsAndId_SourceIdAndDelstats(bukrs, sourceid, "");
    }




    public SourceM findSourceByBukrsAndStageId(int bukrs, int sourceid) {

        return repoSourceM.findById_BukrsAndId_SourceIdAndDelstats(bukrs, sourceid, "");
    }




    public SourceM checkSourceDescExist(int bukrs, String descr) {

        return repoSourceM.findById_BukrsAndDescrAndDelstats(bukrs, descr, "");
    }




    public struct_response_create_source createSource(int bukrs, struct_body_create_source body) {

        SourceM sourceM = new SourceM();
        if (body.getSourceId() > 0) {
            sourceM.setId(new SourceMId(bukrs, body.getSourceId()));
        } else {
            int stageId = getMaxSourceId(bukrs);
            sourceM.setId(new SourceMId(bukrs, stageId));
        }
        sourceM.setDescr(body.getDescr());
        sourceM.setDelstats("");
        repoSourceM.save(sourceM);

        if (body.getSourceId() > 0) {
            return new struct_response_create_source(CommonStrings.api_status_success, "Source updated successfully", sourceM.getId().getSourceId());
        }
        return new struct_response_create_source(CommonStrings.api_status_success, "Source created successfully", sourceM.getId().getSourceId());

    }




    public struct_response_delete_source deleteSource(int bukrs, int sourceid) {

        repoSourceM.updateDelStats(bukrs, sourceid);
        return new struct_response_delete_source(CommonStrings.api_status_success, "Source deleted successfully");
    }




    public struct_response_read_source readSources(int bukrs, int sourceid, String search) {

        int totalRecords = sourceDb.getTotalRecordsSourceCount(bukrs, sourceid, search);
        List<struct_source> list = new ArrayList<>();

        if (totalRecords > 0) {
            list = sourceDb.getTotalRecordsSources(bukrs, sourceid, search);
            return new struct_response_read_source(CommonStrings.api_status_success, "Sources retrieved successfully", totalRecords, list);
        }
        return new struct_response_read_source(CommonStrings.api_status_failure, "No record found", totalRecords, list);
    }




}
