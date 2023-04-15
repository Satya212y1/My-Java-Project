package com.zenscale.zencrm_2.service;


import com.zenscale.zencrm_2.scaledb.AuthDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    @Autowired
    AuthDb authDb;

    @Autowired
    Authenticate authenticate;



    public int get_bukrs_apiKey(String apiKey) {

        return authDb.get_bukrs_apiKey(apiKey);

    }




    public int get_bukrs_token(String token) {

        return authenticate.verify_access_token(token);
    }




}


