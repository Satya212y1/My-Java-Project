package com.zenscale.zencrm_2.service;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.zenscale.zencrm_2.entity.GoogleCalendarAuth;
import com.zenscale.zencrm_2.entity.GoogleCalendarAuthId;
import com.zenscale.zencrm_2.repo.RepoGoogleCalendarAuth;
import com.zenscale.zencrm_2.structures.struct_response_save_refresh_token;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Enumeration;

@Service
public class GoogleAuthService {

    @Autowired
    RepoGoogleCalendarAuth repoGoogleCalendarAuth;

    @Autowired
    AuthService authService;



    public struct_response_save_refresh_token saveRefreshToken(String refreshToken, HttpServletRequest request) {
        // TODO - Getting token/uid from - request.getQueryString() of HttpServletRequest
        JSONObject jsonObject = new JSONObject();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = params.nextElement();
            String paramValue = request.getParameter(paramName);
            if (paramName.equals("state")) {
                jsonObject = new JSONObject(paramValue);
            }
        }
        String token = jsonObject.getString("token");
        String uid = jsonObject.getString("uid");

        // TODO - Saving refresh token
        GoogleCalendarAuth ent = new GoogleCalendarAuth();
        int bukrs = authService.get_bukrs_token(token);
        ent.setId(new GoogleCalendarAuthId(bukrs, uid));
        ent.setRefreshToken(refreshToken);
        repoGoogleCalendarAuth.save(ent);

        return new struct_response_save_refresh_token(1, "Refresh token saved successfully", refreshToken);
    }




}
