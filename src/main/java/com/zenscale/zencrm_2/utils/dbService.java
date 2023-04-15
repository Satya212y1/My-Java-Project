package com.zenscale.zencrm_2.utils;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class dbService {



    @Value("${spring.profiles.active}")
    private String profile;

    @Value("${dev.url}")
    private String devUrl;

    @Value("${dev.username}")
    private String devUser;

    @Value("${dev.password}")
    private String devPass;

    @Value("${prd.url}")
    private String prdUrl;

    @Value("${prd.username}")
    private String prdUser;

    @Value("${prd.password}")
    private String prdPass;




    public String get_profile() {

        return profile;
    }




    public String get_url() {

        return profile.equals("dev") ? devUrl : prdUrl;
    }




    public String get_user() {

        return profile.equals("dev") ? devUser : prdUser;
    }




    public String get_password() {

        return profile.equals("dev") ? devPass : prdPass;
    }






}
