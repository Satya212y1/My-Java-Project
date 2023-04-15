package com.zenscale.zencrm_2.service;

import com.zenscale.zencrm_2.structures.struct_response_get_bukrs;
import com.zenscale.zencrm_2.utils.ApiUrls;
import com.zenscale.zencrm_2.utils.RetrofitUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class Authenticate {

    @Autowired
    RetrofitUtil retrofitUtil;



    public int verify_access_token(String token){

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        JSONObject response = retrofitUtil.getQueryData(ApiUrls.url_get_comapany, map, new struct_response_get_bukrs(), "",ApiUrls.base_url_registration);
        if (response != null && response.get("error").toString().length() == 0) {
            struct_response_get_bukrs resp = (struct_response_get_bukrs) response.get("response");
            return resp.getCompanyCode();
        }
        return 0;
    }


}
