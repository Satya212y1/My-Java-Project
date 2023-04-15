package com.zenscale.zencrm_2.api;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.zenscale.zencrm_2.service.AuthService;
import com.zenscale.zencrm_2.service.GoogleAuthService;
import com.zenscale.zencrm_2.structures.struct_invalid_response;
import com.zenscale.zencrm_2.structures.struct_response_save_refresh_token;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Enumeration;

@Controller
@RequestMapping("/google/calendar/")
public class GoogleAuthController {

    @Autowired
    GoogleAuthService googleAuthService;

    @Value("${google.client.client-id}")
    private String clientId;
    @Value("${google.client.client-secret}")
    private String clientSecret;
    @Value("${google.client.redirectUri}")
    private String redirectURI;

    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    GoogleClientSecrets clientSecrets;
    GoogleAuthorizationCodeFlow flow;

    @Value("this.base.url")
    String baseUrl;



    @RequestMapping("connect")
    public RedirectView connect(@RequestParam(name = "token", required = false, defaultValue = "") String token,
                                @RequestParam(name = "uid", required = false, defaultValue = "") String uid) {
        if (token.length() == 0) {
            return new RedirectView(baseUrl + "google/calendar/invalid_response?token=" + token + "&uid=" + uid);
        }
        if (uid.length() == 0) {
            return new RedirectView(baseUrl + "google/calendar/invalid_response?token=" + token + "&uid=" + uid);
        }
        return new RedirectView(authorize(token, uid));
    }



    @ResponseBody
    @RequestMapping(value = "save_refresh_token", method = RequestMethod.GET, params = "code")
    public struct_response_save_refresh_token getRefreshToken(@RequestParam(value = "code") String code, HttpServletRequest request) {
        String getRefreshToken = "";
        try {
            TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
            getRefreshToken = response.getRefreshToken();
            System.out.println("response.getRefreshToken() = " + response.getRefreshToken());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleAuthService.saveRefreshToken(getRefreshToken, request);
    }



    @ResponseBody
    @GetMapping("invalid_response")
    public struct_invalid_response invalidResponse(@RequestParam(name = "token", required = false, defaultValue = "") String token,
                                                   @RequestParam(name = "uid", required = false, defaultValue = "") String uid) {
        if (token.length() == 0) {
            return new struct_invalid_response(-1, "Token missing");
        }
        if (uid.length() == 0) {
            return new struct_invalid_response(-1, "Uid missing");
        }
        return null;
    }



    private String authorize(String token, String uid) {
        AuthorizationCodeRequestUrl authorizationUrl;
        if (flow == null) {
            GoogleClientSecrets.Details web = new GoogleClientSecrets.Details();
            web.setClientId(clientId);
            web.setClientSecret(clientSecret);
            clientSecrets = new GoogleClientSecrets().setWeb(web);
            try {
                httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            flow = new GoogleAuthorizationCodeFlow.
                    Builder(httpTransport, JSON_FACTORY, clientSecrets, Collections.singleton(CalendarScopes.CALENDAR))
                    .setAccessType("offline")
                    .build();
        }
        authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURI);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        jsonObject.put("uid", uid);
        authorizationUrl.setState(jsonObject.toString());
        System.out.println("cal authorizationUrl->" + authorizationUrl);
        return authorizationUrl.build();
    }




}
