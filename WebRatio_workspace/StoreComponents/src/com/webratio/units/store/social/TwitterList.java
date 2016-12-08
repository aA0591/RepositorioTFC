package com.webratio.units.store.social;

import java.util.GregorianCalendar;

import org.json.JSONObject;

public class TwitterList {

    private String name;
    private GregorianCalendar createdAt;
    private String id;
    private String description;
    private String userName;
    private String userId;

    public TwitterList(JSONObject jsonObject) {
        this.name = jsonObject.optString("name");
        this.createdAt = DateHelper.getCalendarFromString(jsonObject.optString("created_at"), DateHelper.TWITTER_DATE_TIME_FORMAT);
        this.id = jsonObject.optString("id_str");
        this.description = jsonObject.optString("description");
        JSONObject jsonUser = jsonObject.getJSONObject("user");
        if (jsonUser != null) {
            this.userName = jsonUser.optString("name");
            this.userId = jsonUser.optString("id");

        }

    }

    public String getName() {
        return name;
    }

    public GregorianCalendar getCreatedAt() {
        return createdAt;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

}
