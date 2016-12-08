package com.webratio.units.store.social;

import org.json.JSONObject;

public class GoogleCalendarEventReminder {

    public String method;
    public Integer minutes;

    public GoogleCalendarEventReminder(JSONObject reminder) {
        this.method = reminder.optString("method");
        this.minutes = new Integer(reminder.optInt("minutes"));
    }

    public GoogleCalendarEventReminder(String method, Integer minutes) {
        super();
        this.method = method;
        this.minutes = minutes;
    }

    public String getMethod() {
        return method;
    }

    public Integer getMinutes() {
        return minutes;
    }

}
