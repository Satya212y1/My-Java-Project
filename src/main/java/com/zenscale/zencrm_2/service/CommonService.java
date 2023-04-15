package com.zenscale.zencrm_2.service;

import com.zenscale.zencrm_2.scaledb.CommonDb;
import com.zenscale.zencrm_2.structures.struct_list_lead;
import com.zenscale.zencrm_2.structures.struct_stages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class CommonService {

    @Autowired
    CommonDb commonDb;




    public boolean isUidExist(int bukrs, String uid) {

        int i = commonDb.isUidExist(bukrs, uid);
        if (i > 0) {
            return true;
        }
        return false;
    }




    public void orderLeadsByDateDescending(List<struct_list_lead> list) {

        Collections.sort(list, new Comparator<struct_list_lead>() {
            public int compare(struct_list_lead o1, struct_list_lead o2) {

                if (o1.getPostingDate() == null || o2.getPostingDate() == null) {
                    return 0;
                }
                return o2.getCreationDate().compareTo(o1.getCreationDate());
            }
        });
    }




    public void orderStages(List<struct_stages> list) {

        Collections.sort(list, new Comparator<struct_stages>() {
            public int compare(struct_stages o1, struct_stages o2) {

                return o1.getStageId() - o2.getStageId();
            }
        });
    }




    public void orderLeadsByDateAccesnding(List<struct_list_lead> list) {

        Collections.sort(list, new Comparator<struct_list_lead>() {
            public int compare(struct_list_lead o1, struct_list_lead o2) {

                if (o1.getPostingDate() == null || o2.getPostingDate() == null) {
                    return 0;
                }
                return o1.getPostingDate().compareTo(o2.getPostingDate());
            }
        });
    }




}
