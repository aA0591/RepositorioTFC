package com.webratio.units.store.social;

import org.json.JSONObject;

public class GoogleCalendar {

    private String id;
    private String summary;
    private String location;
    private String timeZone;
    private String description;

    /**
     * Constructor used for Update functions
     * 
     * @param id
     * @param summary
     * @param location
     * @param timezone
     * @param description
     */
    public GoogleCalendar(String id, String summary, String location, String timezone, String description) {
        super();
        this.id = id;
        this.summary = summary;
        this.location = location;
        this.timeZone = timezone;
        this.description = description;
    }

    /**
     * Constructor used for Create functions
     * 
     * @param summary
     * @param location
     * @param timezone
     * @param description
     */
    public GoogleCalendar(String summary, String location, String timezone, String description) {
        super();
        this.summary = summary;
        this.location = location;
        this.timeZone = timezone;
        this.description = description;
    }

    public GoogleCalendar(JSONObject jsonCalendar) {
        this.id = jsonCalendar.optString("id");
        this.summary = jsonCalendar.optString("summary");
        this.location = jsonCalendar.optString("location");
        this.timeZone = jsonCalendar.optString("timeZone");
        this.description = jsonCalendar.optString("description");
    }

    public JSONObject getJSONObject() {
        JSONObject result = new JSONObject();
        if (this.id != null)
            result.put("id", this.id);
        if (summary != null && !summary.equals(""))
            result.put("summary", this.summary);

        if (location != null && !location.equals(""))
            result.put("location", this.location);

        if (timeZone != null && !timeZone.equals(""))
            result.put("timeZone", this.timeZone);

        if (description != null && !description.equals(""))
            result.put("description", this.description);

        return result;
    }

    public String getId() {
        return id;
    }

    public String getSummary() {
        return summary;
    }

    public String getLocation() {
        return location;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getDescription() {
        return description;
    }

}
