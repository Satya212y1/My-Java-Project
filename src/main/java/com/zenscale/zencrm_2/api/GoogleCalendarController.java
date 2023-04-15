package com.zenscale.zencrm_2.api;

import com.zenscale.zencrm_2.service.AuthService;
import com.zenscale.zencrm_2.service.GoogleCalendarService;
import com.zenscale.zencrm_2.structures.struct_response;
import com.zenscale.zencrm_2.structures.struct_response_read_events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("api/crm/google/calendar/event/")
public class GoogleCalendarController {

    @Autowired
    GoogleCalendarService googleCalendarService;

    @Autowired
    AuthService authService;



    @GetMapping("read")
    public struct_response_read_events readEvents(@RequestHeader(name = "Authorization", required = false, defaultValue = "") String token,
                                                  @RequestParam(name = "uid", required = false, defaultValue = "") String uid) throws IOException, GeneralSecurityException {
        if (token.length() == 0) {
            return new struct_response_read_events(-1, "Authorization missing");
        }
        int bukrs = authService.get_bukrs_token(token);
        if (bukrs == 0) {
            return new struct_response_read_events(-1, "Invalid token");
        }
        return googleCalendarService.findEvents(bukrs, uid);
    }




}
