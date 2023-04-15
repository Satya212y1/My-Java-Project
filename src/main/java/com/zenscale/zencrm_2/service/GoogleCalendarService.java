package com.zenscale.zencrm_2.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.gson.JsonArray;
import com.zenscale.zencrm_2.entity.GoogleCalendarAuth;
import com.zenscale.zencrm_2.entity.GoogleCalendarAuthId;
import com.zenscale.zencrm_2.repo.RepoGoogleCalendarAuth;
import com.zenscale.zencrm_2.structures.struct_response_read_events;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
public class GoogleCalendarService {

    @Autowired
    RepoGoogleCalendarAuth repoGoogleCalendarAuth;

    private static final String APPLICATION_NAME = "Zenscale-Calendar-Task";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static HttpTransport httpTransport;
    private static Calendar client;

    @Value("${google.client.client-id}")
    private String clientId;
    @Value("${google.client.client-secret}")
    private String clientSecret;
    @Value("${google.client.redirectUri}")
    private String redirectURI;

    final DateTime date1 = new DateTime("2022-05-05T16:30:00.000+05:30");
    final DateTime date2 = new DateTime("2024-05-05T16:30:00.000+05:30");



    public struct_response_read_events findEvents(int bukrs, String uid) throws IOException, GeneralSecurityException {
        Optional<GoogleCalendarAuth> calendarAuth = repoGoogleCalendarAuth.findById(new GoogleCalendarAuthId(bukrs, uid));
        String message = "";

        if (calendarAuth.isPresent()) {
            String refreshToken = calendarAuth.get().getRefreshToken();

            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(refreshToken))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            // List the next 10 events from the primary calendar.
            DateTime now = new DateTime(System.currentTimeMillis());
            Events events = service.events().list("primary")
                    .setMaxResults(100)
                    .setTimeMin(date1)
                    .setTimeMax(date2)
                    .execute();
            List<Event> items = events.getItems();
            if (items.isEmpty()) {
                System.out.println("No upcoming events found.");
            } else {
                System.out.println("Upcoming events");
                for (Event event : items) {
                    DateTime start = event.getStart().getDateTime();
                    if (start == null) {
                        start = event.getStart().getDate();
                    }
                    System.out.println("Summary: " + event.getSummary());
                    System.out.println("Desc: " + event.getDescription());
                    System.out.println("HtmlLink: " + event.getHtmlLink());
                    System.out.println("-----------------------------------------------------------------------------------------------------------");
                }
            }
        }

        return new struct_response_read_events(1, "", message);
    }



    private GoogleCredential getCredentials(String refreshToken) {
        ArrayList<String> scopes = new ArrayList<>();
        scopes.add(CalendarScopes.CALENDAR);
        TokenResponse tokenResponse = null;
        try {
            tokenResponse = new GoogleRefreshTokenRequest(
                    new NetHttpTransport(),
                    new JacksonFactory(),
                    refreshToken,
                    clientId,
                    clientSecret)
                    .setScopes(scopes)
                    .setGrantType("refresh_token")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("getAccessToken() = " + tokenResponse.getAccessToken());
        GoogleCredential credential = new GoogleCredential().setFromTokenResponse(tokenResponse);
        return credential;
    }




}
