package com.zenscale.zencrm_2.service;

import com.zenscale.zencrm_2.structures.*;
import com.zenscale.zencrm_2.utils.ApiUrls;
import com.zenscale.zencrm_2.utils.RetrofitUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RegistrationService {


    @Autowired
    RetrofitUtil retrofitUtil;




    public JSONObject getUsersDetailByUid(String uid) {

        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);

        JSONObject response = retrofitUtil
                .getQueryData(
                        ApiUrls.url_get_read_user,
                        map,
                        new struct_response_read_by_uid(),
                        "",
                        ApiUrls.base_url_registration
                );

        if (response != null && response.get("error").toString().length() == 0) {
            struct_response_read_by_uid res = (struct_response_read_by_uid) response.get("response");
            return new JSONObject(res);
        }
        return null;
    }




    public sr_read_users getAllUsersInCompany(String token) {
        //System.out.println("token = " + token);
        Map<String, Object> map = new HashMap<>();

        JSONObject response = retrofitUtil
                .getQueryData(
                        ApiUrls.url_get_read_all_user,
                        map,
                        new sr_read_users(),
                        token,
                        ApiUrls.base_url_registration
                );

        if (response != null && response.get("error").toString().length() == 0) {
            sr_read_users res = (sr_read_users) response.get("response");
            return res;
        }
        return null;
    }




    public Map<String, sl_users> getMapUsers(String token) {
        sr_read_users res = getAllUsersInCompany(token);
        Map<String, sl_users> info = new HashMap<>();
        if (res != null && res.getUsersList() != null && res.getUsersList().size() > 0) {
            info = res.getUsersList().stream().distinct().collect(Collectors.toMap(s -> s.getUid(), s -> s));
        }
        return info;
    }




}
