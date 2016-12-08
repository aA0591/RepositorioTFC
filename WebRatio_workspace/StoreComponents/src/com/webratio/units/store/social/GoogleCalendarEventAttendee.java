package com.webratio.units.store.social;

import org.json.JSONObject;

public class GoogleCalendarEventAttendee {

    private String id;
    private String email;
    private String displayName;

    public GoogleCalendarEventAttendee(JSONObject attendee) {
        this.id = attendee.optString("id");
        this.email = attendee.optString("email");
        this.displayName = attendee.optString("displayName");
    }

    public GoogleCalendarEventAttendee(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

}
