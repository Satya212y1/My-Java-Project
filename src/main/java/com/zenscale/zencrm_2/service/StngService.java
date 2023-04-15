package com.zenscale.zencrm_2.service;

import com.zenscale.zencrm_2.entity.stngs_bukrs;
import com.zenscale.zencrm_2.repo.RepoStng;
import com.zenscale.zencrm_2.repo.RepoStngsTaParam;
import com.zenscale.zencrm_2.scaledb.StngDb;
import com.zenscale.zencrm_2.structures.struct_stngs;
import com.zenscale.zencrm_2.utils.dbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StngService {


    @Autowired
    RepoStng repoStng;

    @Autowired
    RepoStngsTaParam repoStngsTaParam;

    @Autowired
    StngDb sd;




    public struct_stngs save_stng(int bukrs, struct_stngs stngs) {

        stngs_bukrs stngs_bukrs = new stngs_bukrs(bukrs, stngs);
        stngs_bukrs s = repoStng.save(stngs_bukrs);
        return new struct_stngs(s);
    }




    public struct_stngs get_stngs(int bukrs, String stngid) {


        List<struct_stngs> data = sd.get_stngs(bukrs, stngid, "", "");
        if (data.size() <= 0) {
            return null;
        } else {
            return data.get(0);
        }

    }




    public boolean checkJobOrderDataExist(int bukrs) {

        return sd.checkJobOrderDataExist(bukrs);
    }




    public int checkSettingId(String stngid) {

        return sd.checkExist(stngid);
    }




    public List<struct_stngs> get_all_stngs(int bukrs, String type, String parent_stng_id) {

        return sd.get_stngs(bukrs, "", type, parent_stng_id);
    }




}
